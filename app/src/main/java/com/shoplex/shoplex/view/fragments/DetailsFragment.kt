package com.shoplex.shoplex.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.R
import com.shoplex.shoplex.databinding.FragmentDetailsBinding
import com.shoplex.shoplex.model.adapter.ChatHeadAdapter
import com.shoplex.shoplex.model.adapter.PropertyAdapter
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.extra.ArchLifecycleApp
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.interfaces.FavouriteCartListener
import com.shoplex.shoplex.model.maps.LocationManager
import com.shoplex.shoplex.model.pojo.*
import com.shoplex.shoplex.room.data.ShopLexDataBase
import com.shoplex.shoplex.room.repository.FavoriteCartRepo
import com.shoplex.shoplex.view.activities.CheckOutActivity
import com.shoplex.shoplex.view.activities.DetailsActivity
import com.shoplex.shoplex.view.activities.MessageActivity
import com.shoplex.shoplex.viewmodel.DetailsVM
import com.shoplex.shoplex.viewmodel.ProductsVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DetailsFragment : Fragment(), FavouriteCartListener {
    private lateinit var binding: FragmentDetailsBinding
    private var product: Product = Product()
    private val imageList = ArrayList<SlideModel>()

    private lateinit var productsVM: ProductsVM
    private lateinit var detailsVM: DetailsVM

    private lateinit var repo: FavoriteCartRepo
    private lateinit var lifecycleScope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        lifecycleScope = requireActivity().lifecycleScope
        repo = FavoriteCartRepo(ShopLexDataBase.getDatabase(binding.root.context).shopLexDao())

        productsVM = (requireActivity() as DetailsActivity).productsVM
        detailsVM = (requireActivity() as DetailsActivity).detailsVM

        if (productsVM.products.value == null)
            productsVM.getProduct()
        else if (productsVM.products.value!!.isNotEmpty())
            product = productsVM.products.value!!.first()

        productsVM.products.observe(this.viewLifecycleOwner, { products ->
            if (products.isNotEmpty()) {
                this.product = products.first()
                detailsVM.getStoreData(product.storeID)
                binding.product = product
                imageList.clear()
                for (img in product.images)
                    imageList.add(SlideModel(img))
                if (product.images.isEmpty())
                    imageList.add(SlideModel(R.drawable.product))
                binding.imgSliderDetails.setImageList(imageList, ScaleTypes.CENTER_CROP)
                binding.rvProperty.adapter = PropertyAdapter(product.properties, requireContext())
                onSearchForFavouriteCart(product.productID)
                if (product.quantity == 0) {
                    binding.linearLayout.isEnabled = false
                    binding.linearLayout.visibility = View.INVISIBLE
                    onDeleteFromCart(product.productID)
                }
                productsVM.products.removeObservers(this)
            }
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
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_cart),
                    null
                )
                product.isCart = false
            } else {
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_done),
                    null
                )
                product.isCart = true
            }
        })

        binding.btnCall.setOnClickListener {
            if(detailsVM.store.value != null) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data =
                    Uri.parse(getString(R.string.telephone) + detailsVM.store.value!!.phone)
                startActivity(intent)
            } else {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.Telephone), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()
            }
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
            if(!ArchLifecycleApp.isInternetConnected) {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.NoInternetConnection), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()
                return@setOnClickListener
            }
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
                                detailsVM.store.value!!.phone,
                                true,
                                productIDs = arrayListOf(productsVM.productID.value!!),
                                storeImage = detailsVM.store.value!!.image
                            )
                            newChatRef.set(chat).addOnSuccessListener {
                                initMessage(chat.chatID)
                                openMessagesActivity(chat.chatID)
                            }
                        }
                        values.count() == 1 -> {
                            val chat: Chat = values.first().toObject()
                            if (chat.productIDs.last() != productsVM.productID.value!!)
                                initMessage(chat.chatID)
                            FirebaseReferences.chatRef.document(chat.chatID)
                                .update(
                                    "productIDs",
                                    FieldValue.arrayUnion(productsVM.productID.value!!)
                                )
                            openMessagesActivity(chat.chatID)
                        }
                        else -> {
                            val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.ERROR), Snackbar.LENGTH_LONG)
                            val sbView: View = snackbar.view
                            sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                            snackbar.show()

                        }
                    }

                }
        }

        binding.imgLocation.setOnClickListener {
            LocationManager.getInstance(requireContext()).launchGoogleMaps(product.storeLocation)
        }

        binding.btnBuyProduct.setOnClickListener {
            if (UserInfo.userID != null) {
                if (ArchLifecycleApp.isInternetConnected) {
                    val selectedProperties: ArrayList<String> = arrayListOf()
                    for (property in product.properties) {
                        if (property.selectedProperty != null)
                            selectedProperties.add(property.selectedProperty!!)
                    }

                    startActivity(Intent(context, CheckOutActivity::class.java).apply {
                        this.putParcelableArrayListExtra(
                            CheckOutActivity.PRODUCTS_QUANTITY,
                            arrayListOf<ProductQuantity>().apply {
                                this.add(ProductQuantity(product.productID, 1))
                            })

                        if (selectedProperties.isNotEmpty())
                            this.putStringArrayListExtra(
                                CheckOutActivity.PRODUCT_PROPERTIES,
                                selectedProperties
                            )

                        this.putExtra("isBuyNow", true)

                    })
                }else {
                    val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.NoInternetConnection), Snackbar.LENGTH_LONG)
                    val sbView: View = snackbar.view
                    sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                    snackbar.show()
                }


            } else {
                val snackbar = Snackbar.make(binding.root, binding.root.context.getString(R.string.pleaseLogin), Snackbar.LENGTH_LONG)
                val sbView: View = snackbar.view
                sbView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blueshop))
                snackbar.show()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            if (product.isCart) {
                onDeleteFromCart(product.productID)
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_cart),
                    null
                )
                product.isCart = false
            } else {
                onAddToCart(ProductCart(product = product))
                binding.btnAddToCart.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    AppCompatResources.getDrawable(requireContext(), R.drawable.ic_done),
                    null
                )
                product.isCart = true
            }
        }

        return binding.root
    }

    private fun openMessagesActivity(chatID: String) {
        binding.root.context.startActivity(
            Intent(
                binding.root.context,
                MessageActivity::class.java
            ).apply {
                this.putExtra(ChatHeadAdapter.CHAT_TITLE_KEY, product.storeName)
                this.putExtra(ChatHeadAdapter.CHAT_IMG_KEY, product.images.firstOrNull())
                this.putExtra(ChatHeadAdapter.CHAT_ID_KEY, chatID)
                this.putExtra(ChatHeadAdapter.PRODUCT_ID, product.productID)
                this.putExtra(ChatHeadAdapter.STORE_ID_KEY, product.storeID)
                this.putExtra(ChatHeadAdapter.STORE_PHONE, detailsVM.store.value!!.phone)
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