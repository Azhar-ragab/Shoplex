package com.shoplex.shoplex.model.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.RvHomeProductCardviewBinding
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.maps.LocationManager
import com.shoplex.shoplex.model.maps.RouteInfo
import com.shoplex.shoplex.model.pojo.*
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import com.shoplex.shoplex.view.activities.DetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeAdapter(var productsHome: ArrayList<Product>) :
    RecyclerView.Adapter<HomeAdapter.ProductViewHolder>() {
    private lateinit var context: Context
    private var originalProducts: ArrayList<Product> = arrayListOf()

    private lateinit var repo: FavoriteCartRepo
    private lateinit var lifecycleScope: CoroutineScope

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        context = parent.context
        lifecycleScope = (context as AppCompatActivity).lifecycleScope
        repo = FavoriteCartRepo(ShopLexDataBase.getDatabase(context).shoplexDao())

        return ProductViewHolder(
            RvHomeProductCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(productsHome[position])

    override fun getItemCount() = productsHome.size

    fun search(searchText: String) {
        if (!searchText.isNullOrEmpty()) {
            if (originalProducts.isEmpty())
                originalProducts = productsHome
            productsHome = originalProducts.filter {
                it.name.contains(
                    searchText,
                    true
                ) || it.storeName.contains(searchText, true)
            } as ArrayList<Product>
        } else {
            productsHome = originalProducts
        }
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(val binding: RvHomeProductCardviewBinding) :
        RecyclerView.ViewHolder(binding.root), FavouriteCartListener {
        fun bind(product: Product) {
            val user: User = User()

            // onSearchForFavourite(product.productID)

            //val viewModel = ViewModelProvider(context as AppCompatActivity, FavouriteFactoryModel(context, product.productID)).get(FavouriteViewModel::class.java)

            if(product.quantity == 0){
                binding.btnFavorite.visibility = View.INVISIBLE
                binding.fabAddProduct.visibility = View.INVISIBLE
            }

            repo.searchFavouriteByID.observe(context as AppCompatActivity, {
                if (it == null) {
                    binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite)
                    product.isFavourite = false
                } else {
                    binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_fill)
                    product.isFavourite = true
                }
            })

            repo.searchCartByID.observe(context as AppCompatActivity, {
                if (it == null) {
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_cart))
                    product.isCart = false
                } else {
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_done))
                    product.isCart = true
                }
            })

            repo.storeLocationInfo.observe(context as AppCompatActivity, {
                if (it != null) {
                    binding.tvSpace.text = it.distance
                } else {
                    findRoute(product.storeID, product.storeName, product.storeLocation)
                }
            })

            onSearchForFavouriteCart(product.productID)
            onFindingRoute(StoreLocationInfo(storeID = product.storeID, location = product.storeLocation))

            binding.btnFavorite.setOnClickListener {
                if (product.isFavourite) {
                    onDeleteFromFavourite(product.productID)
                    binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite)
                    product.isFavourite = false
                } else {
                    onAddToFavourite(ProductFavourite(product))
                    binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_fill)
                    product.isFavourite = true
                }
                /*
                if (addfavClick!=null){
                    var favourite=ProductFavourite(product)
                    addfavClick.onaddFavourite(favourite)
                }
                 */

                //var favourite=ProductFavourite(product)
                //addfavClick.onaddFavourite(favourite)
                //onSearchForFavourite(product.productID)

                // 7cca2006-fbcf-4287-8e0d-43baa6f65e85
//                binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_fill)
//                binding.btnFavorite.isClickable = false
//                notifyDataSetChanged()
//                UserInfo.favouriteList.add(product.productID)
//                if (addfavClick!=null){
//                    var favourite=ProductFavourite(product)
//                    addfavClick!!.onaddFavourite(favourite)
//                }
//                FirebaseReferences.usersRef.document(UserInfo.userID.toString()).update(
//                    "favouriteList",
//                    FieldValue.arrayUnion(product.productID)
//                )

            }

            binding.fabAddProduct.setOnClickListener {
                if (product.isCart) {
                    onDeleteFromCart(product.productID)
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_cart))
                    product.isCart = false
                } else {
                    onAddToCart(ProductCart(product = product))
                    binding.fabAddProduct.setImageDrawable(context.getDrawable(R.drawable.ic_done))
                    product.isCart = true
                }

//                user.cartList.add(product.productID)
//                var cart=ProductCart(product = product)
//                onAddToCart(cart)
//
//              //  cartVm=ViewModelProvider(binding.root.).get(CartViewModel::class.java)
//                Toast.makeText(binding.root.context,product.productID,Toast.LENGTH_SHORT).show()
//                FirebaseReferences.usersRef.whereEqualTo(binding.root.context.getString(R.string.mail),Firebase.auth.currentUser.email).get().addOnSuccessListener { result ->
//                    for (document in result){
//                        if (document.exists()) {
//                            val u = document.toObject<User>()
//                            FirebaseReferences.usersRef.document(u.userID).update(
//                                binding.root.context.getString(R.string.cartList),
//                                FieldValue.arrayUnion(user.cartList[0])
//
//                            )
//                        }
//                    }
//                }
            }

            binding.product = product

            /*
            binding.tvStorename.text = product.storeName
            binding.tvNewPrice.text = product.newPrice.toString()
            binding.tvOldPrice.text = product.price.toString()
            binding.tvProductName.text = product.name
            binding.tvReview.text = product.rate.toString()
            binding.tvSold.text = R.string.twelve.toString()
            binding.tvSpace.text = R.string.Space.toString()
            */

            Glide.with(binding.root.context).load(product.images.firstOrNull())
                .error(R.drawable.product).into(binding.imgProduct)

            itemView.setOnClickListener {
                var intent: Intent = Intent(binding.root.context, DetailsActivity::class.java)
                intent.putExtra(
                    binding.root.context.getString(R.string.productId),
                    product.productID
                )
                binding.root.context.startActivity(intent)
            }



        }

        private fun findRoute(storeID: String, storeName: String, storeLocation: Location) {
            GlobalScope.launch(Dispatchers.IO) {
                val info: RouteInfo? = LocationManager.getInstance(context).getRouteInfo(
                    UserInfo.location,
                    storeLocation
                )

                var res = "N/A"

                if(info != null){
                    val locationInfo = StoreLocationInfo(storeID, storeLocation, storeName, info.distance, info.duration)
                    onAddStoreInfo(locationInfo)
                    res = info.distance!!
                }

                (context as AppCompatActivity).runOnUiThread {
                    binding.tvSpace.text = res
                }

                Log.i("LOCATIONINFO", info.toString())
            }
        }

        override fun onAddToCart(productCart: ProductCart) {
            super.onAddToCart(productCart)
            lifecycleScope.launch {
                productCart.cartQuantity = 1
                repo.addCart(productCart)
            }
        }

        override fun onDeleteFromCart(productID: String) {
            super.onDeleteFromCart(productID)
            lifecycleScope.launch {
                repo.deleteCart(productID)
            }
        }

        override fun onSearchForFavouriteCart(productId: String) {
            repo.productID.value = productId
            //viewModelScope.launch(Dispatchers.IO) {
            //favouriteRepo.addFavourite(favourite)
            //}
            //   repo.searchFavourite(productId)
        }

        override fun onAddToFavourite(productFavourite: ProductFavourite) {
            super.onAddToFavourite(productFavourite)
            lifecycleScope.launch {
                repo.addFavourite(productFavourite)
            }
            //favouriteViewModel.addFavourite(productFavourite)
        }

        override fun onDeleteFromFavourite(productID: String) {
            super.onDeleteFromFavourite(productID)
            lifecycleScope.launch {
                repo.deleteFavourite(productID)
            }
        }

        override fun onAddStoreInfo(storeLocationInfo: StoreLocationInfo) {
            lifecycleScope.launch {
                repo.addNewLocation(storeLocationInfo)
            }
        }

        override fun onFindingRoute(storeLocation: StoreLocationInfo) {
            lifecycleScope.launch {
                repo.storeIfo.value = storeLocation
            }
        }
    }
}