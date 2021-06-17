package com.shoplex.shoplex.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDetailsBinding
import com.shoplex.shoplex.model.adapter.PropertyAdapter
import com.shoplex.shoplex.model.enumurations.keys.ChatMessageKeys
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.pojo.*
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.view.activities.MapsActivity
import com.shoplex.shoplex.view.activities.MessageActivity
import com.shoplex.shoplex.viewmodel.DetailsVM
import com.shoplex.shoplex.viewmodel.OrdersVM
import com.shoplex.shoplex.viewmodel.ProductsVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetailsFragment(val productId: String) : Fragment(), FavouriteCartListener {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var productsVM: ProductsVM
    private var product: Product = Product()
    private lateinit var detailsVM: DetailsVM
    private var storeInfo: Store = Store()
    private val imageList = ArrayList<SlideModel>() // Create image list
    private val CHAT_TITLE_KEY = "CHAT_TITLE_KEY"
    private val MAPS_CODE = 202
    private lateinit var ref: DocumentReference

    private lateinit var repo: FavoriteCartRepo
    private lateinit var lifecycleScope: CoroutineScope

    private val ordersNM = OrdersVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        lifecycleScope = (context as AppCompatActivity).lifecycleScope
        repo = FavoriteCartRepo(ShopLexDataBase.getDatabase(binding.root.context).shoplexDao())

        this.productsVM = ProductsVM()
        this.detailsVM = DetailsVM()
        ref = FirebaseReferences.chatRef.document()
        productsVM.getProductById(productId)
        productsVM.products.observe(this.viewLifecycleOwner,  {
            if (it.count() > 0) {
                this.product = it[0]
                detailsVM.getStoreData(product.storeID)
                binding.product = product
                for (img in product.images)
                    imageList.add(SlideModel(img))
                binding.imgSliderDetails.setImageList(imageList, ScaleTypes.CENTER_CROP)
                propertyAdapter = context?.let { PropertyAdapter(product.properties, it) }!!
                binding.rvProperty.adapter = propertyAdapter
                onSearchForFavouriteCart(product.productID)
            }

        })

        detailsVM.store.observe(this.viewLifecycleOwner, Observer {
            this.storeInfo = it
        })

        repo.searchFavouriteByID.observe(context as AppCompatActivity, {
            if (it == null) {
                binding.btnFavourite.setBackgroundResource(R.drawable.ic_favorite)
                product.isFavourite = false
            } else {
                binding.btnFavourite.setBackgroundResource(R.drawable.ic_favorite_fill)
                product.isFavourite = true
            }
        })

        repo.searchCartByID.observe(context as AppCompatActivity, {
            if (it == null) {
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(null, null, requireContext().getDrawable(R.drawable.ic_cart), null)
                product.isCart = false
            } else {
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(null, null, requireContext().getDrawable(R.drawable.ic_done), null)
                product.isCart = true
            }
        })

        /*
        repo.storeLocationInfo.observe(context as AppCompatActivity, {
            if (it != null) {
                //product.
            } else {
                findRoute(product.storeID, product.storeName, product.storeLocation)
            }
        })
        */

        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse(getString(R.string.telephone) + storeInfo.phone)
            startActivity(intent)
        }

        binding.btnFavourite.setOnClickListener {
            if (product.isFavourite) {
                onDeleteFromFavourite(product.productID)
                binding.btnFavourite.setBackgroundResource(R.drawable.ic_favorite)
                product.isFavourite = false
            } else {
                onAddToFavourite(ProductFavourite(product))
                binding.btnFavourite.setBackgroundResource(R.drawable.ic_favorite_fill)
                product.isFavourite = true
            }
        }

        binding.btnMessage.setOnClickListener {
            var intent: Intent = Intent(binding.root.context, MessageActivity::class.java)
            intent.putExtra(ChatMessageKeys.CHAT_TITLE_KEY.name, product.storeName)

            intent.putExtra(ChatMessageKeys.PRODUCT_ID.name, product.productID)
            intent.putExtra(ChatMessageKeys.STORE_ID.name, product.storeID)
            // intent.putExtra(ChatMessageKeys.PHONE.name, storeInfo.phone)
            intent.putExtra(ChatMessageKeys.CHAT_IMG_KEY.name, product.images[0])

            FirebaseReferences.chatRef.whereEqualTo("storeID", product.storeID)
                .whereEqualTo("userID", UserInfo.userID).get().addOnSuccessListener { values ->
                    if (values.count() == 0) {
                        var chat = Chat(
                            ref.id,
                            UserInfo.userID.toString(),
                            UserInfo.name,
                            product.storeID,
                            arrayListOf(productId)
                        )
                        ref.set(chat).addOnSuccessListener {
                            initMessage(chat.chatID)
                            intent.putExtra(ChatMessageKeys.CHAT_ID.name, ref.id)
                            binding.root.context.startActivity(intent)
                        }
                    } else if (values.count() == 1) {
                        val chat: Chat = values.first().toObject()
                        // chat.productIDs.add(product.productID)
                        if(chat.productIDs.last() != productId)
                            initMessage(chat.chatID)
                        FirebaseReferences.chatRef.document(chat.chatID).update("productIDs", FieldValue.arrayUnion(productId))
                        intent.putExtra(ChatMessageKeys.CHAT_ID.name, chat.chatID)
                        binding.root.context.startActivity(intent)
                    } else {
                        Toast.makeText(context, getString(R.string.ERROR), Toast.LENGTH_SHORT)
                            .show()
                    }

                }
        }

        binding.imgLocation.setOnClickListener {
            val location: Location = storeInfo.locations!![0]
            var intent: Intent = Intent(binding.root.context, MapsActivity::class.java)
            intent.putExtra(getString(R.string.locationLat), location.latitude)
            intent.putExtra(getString(R.string.locationLang), location.longitude)
            intent.putExtra(getString(R.string.storeName), product.storeName)
            startActivityForResult(intent, MAPS_CODE)
        }

        binding.btnBuyProduct.setOnClickListener {
            if (UserInfo.userID != null) {
                startActivity(Intent(context, CheckOutActivity::class.java).apply {
                    this.putParcelableArrayListExtra("PRODUCTS_QUANTITY", arrayListOf<ProductQuantity>().apply {
                        this.add(ProductQuantity(product.productID, 1))
                    })

                    this.putExtra("isBuyNow", true)
                })
            } else {
                Toast.makeText(context, getString(R.string.validation), Toast.LENGTH_SHORT).show()
            }

            /*
            var specialDiscount: SpecialDiscount = SpecialDiscount(10F, DiscountType.Fixed)

            //product.quantity = 1
            var productCart: ProductCart = ProductCart(product, 1, specialDiscount)
            //productCart.specialDiscount = specialDiscount

            val address: String? = LocationManager.getInstance(requireContext()).getAddress(
                LatLng(
                    UserInfo.location.latitude, UserInfo.location.longitude
                ), requireContext()
            )
            */

            /*
            var checkout: Checkout = Checkout(
                DeliveryMethod.Door, PaymentMethod.Cash, Location(
                    UserInfo.location.latitude, UserInfo.location.longitude
                ), address ?: "", product.price, 12
            )
            checkout.addProduct(productCart)


            for (product in checkout.getAllProducts()) {
                var order: Order = Order(product)
                ordersNM.addOrder(order)
            }

             */
        }

        binding.btnAddToCart.setOnClickListener {
            if (product.isCart) {
                onDeleteFromCart(product.productID)
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(null, null, requireContext().getDrawable(R.drawable.ic_cart), null)
                product.isCart = false
            } else {
                onAddToCart(ProductCart(product = product))
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(null, null, requireContext().getDrawable(R.drawable.ic_done), null)
                product.isCart = true
            }
        }

        return binding.root
    }

    private fun initMessage(chatID: String) {
        var message = Message(
            toId = UserInfo.userID!!, message = "You started new chat for " + product.name
        )
        FirebaseReferences.chatRef.document(chatID).collection(getString(R.string.messages))
            .document(message.messageID)
            .set(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAPS_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val location: Parcelable? = data?.getParcelableExtra(getString(R.string.Loc))
                if (location != null) {

                }
            }
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

    override fun onAddToFavourite(productFavourite: ProductFavourite) {
        super.onAddToFavourite(productFavourite)
        lifecycleScope.launch {
            repo.addFavourite(productFavourite)
        }
    }

    override fun onDeleteFromFavourite(productID: String) {
        super.onDeleteFromFavourite(productID)
        lifecycleScope.launch {
            repo.deleteFavourite(productID)
        }
    }

    override fun onSearchForFavouriteCart(productId: String) {
        repo.productID.value = productId
    }
}