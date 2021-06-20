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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDetailsBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.PropertyAdapter
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
import com.shoplex.shoplex.viewmodel.ProductsVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetailsFragment(private val productId: String) : Fragment(), FavouriteCartListener {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var productsVM: ProductsVM
    private var product: Product = Product()
    private lateinit var detailsVM: DetailsVM
    private var storeInfo: Store = Store()
    private val imageList = ArrayList<SlideModel>()
    private val MAPS_CODE = 202

    private lateinit var repo: FavoriteCartRepo
    private lateinit var lifecycleScope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        lifecycleScope = (context as AppCompatActivity).lifecycleScope
        repo = FavoriteCartRepo(ShopLexDataBase.getDatabase(binding.root.context).shopLexDao())

        this.productsVM = ViewModelProvider(this).get(ProductsVM::class.java)
        this.detailsVM = ViewModelProvider(this).get(DetailsVM::class.java)

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

        detailsVM.store.observe(this.viewLifecycleOwner, {
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
            FirebaseReferences.chatRef.whereEqualTo("storeID", product.storeID)
                .whereEqualTo("userID", UserInfo.userID).get().addOnSuccessListener { values ->
                    when {
                        values.count() == 0 -> {
                            val newChatRef = FirebaseReferences.chatRef.document()
                            val chat = Chat(
                                newChatRef.id,
                                UserInfo.userID!!,
                                product.storeID,
                                UserInfo.name,
                                product.storeName,
                                storeInfo.phone,
                                true,
                                productIDs = arrayListOf(productId),
                                storeImage = storeInfo.image
                            )
                            newChatRef.set(chat).addOnSuccessListener {
                                initMessage(chat.chatID)
                                openMessagesActivity(chat.chatID)
                            }
                        }
                        values.count() == 1 -> {
                            val chat: Chat = values.first().toObject()
                            if (chat.productIDs.last() != productId)
                                initMessage(chat.chatID)
                            FirebaseReferences.chatRef.document(chat.chatID)
                                .update("productIDs", FieldValue.arrayUnion(productId))
                            openMessagesActivity(chat.chatID)
                        }
                        else -> {
                            Toast.makeText(context, getString(R.string.ERROR), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }
        }

        binding.imgLocation.setOnClickListener {
            val location: Location = storeInfo.locations!![0]
            val intent = Intent(binding.root.context, MapsActivity::class.java)
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

    private fun openMessagesActivity(chatID: String){
        binding.root.context.startActivity(Intent(binding.root.context, MessageActivity::class.java).apply {
            this.putExtra(ChatHeadAdapter.CHAT_TITLE_KEY, product.storeName)
            this.putExtra(ChatHeadAdapter.CHAT_IMG_KEY, product.images.firstOrNull())
            this.putExtra(ChatHeadAdapter.CHAT_ID_KEY, chatID)
            this.putExtra(ChatHeadAdapter.PRODUCT_ID, product.productID)
            this.putExtra(ChatHeadAdapter.STORE_ID_KEY, product.storeID)
            this.putExtra(ChatHeadAdapter.STORE_PHONE, storeInfo.phone)
        })
    }

    private fun initMessage(chatID: String) {
        val message = Message(
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