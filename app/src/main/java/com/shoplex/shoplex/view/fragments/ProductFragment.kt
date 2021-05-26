package com.shoplex.shoplex

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
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import com.shoplex.shoplex.databinding.FragmentProductBinding
import com.shoplex.shoplex.model.enumurations.DeliveryMethod
import com.shoplex.shoplex.model.enumurations.DiscountType
import com.shoplex.shoplex.model.enumurations.OrderStatus
import com.shoplex.shoplex.model.enumurations.PaymentMethod
import com.shoplex.shoplex.model.extra.FirebaseReferences
import com.shoplex.shoplex.model.extra.User
import com.shoplex.shoplex.model.extra.UserInfo
import com.shoplex.shoplex.model.pojo.*
import com.shoplex.shoplex.view.activities.MapsActivity
import com.shoplex.shoplex.view.activities.MessageActivity
import com.shoplex.shoplex.viewmodel.DetailsVM
import com.shoplex.shoplex.viewmodel.OrdersVM
import com.shoplex.shoplex.viewmodel.ProductsVM


class ProductFragment(val productId: String) : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentProductBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var productsVM: ProductsVM
    private lateinit var product: Product
    private lateinit var detailsVM: DetailsVM
    private var storeInfo: Store = Store()
    private val imageList = ArrayList<SlideModel>() // Create image list
    private val CHAT_TITLE_KEY = "CHAT_TITLE_KEY"
    private val MAPS_CODE = 202
    private lateinit var ref: DocumentReference


    private val ordersNM = OrdersVM()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentProductBinding.inflate(inflater, container, false)
        this.productsVM = ProductsVM()
        this.detailsVM = DetailsVM()
        ref = FirebaseReferences.chatRef.document()
        productsVM.getProductById(productId)
        productsVM.products.observe(this.viewLifecycleOwner, Observer {
            if (it.count() > 0) {
                this.product = it[0]
                detailsVM.getStoreData(product.storeID)
                binding.product = product
                for (img in product.images)
                    imageList.add(SlideModel(img))
                binding.imgSliderDetails.setImageList(imageList, ScaleTypes.CENTER_CROP)
                propertyAdapter = context?.let { PropertyAdapter(product.properties, it) }!!
                binding.rvProperty.adapter = propertyAdapter

            }

        })
        detailsVM.store.observe(this.viewLifecycleOwner, Observer {
            this.storeInfo = it
        })
        binding.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:${storeInfo.phone}")
            startActivity(intent)

        }
        binding.btnMessage.setOnClickListener {
            var intent: Intent = Intent(binding.root.context, MessageActivity::class.java)
            intent.putExtra("storeID", product.storeID)
            intent.putExtra(CHAT_TITLE_KEY, product.storeName)
            intent.putExtra("phoneNumber", storeInfo.phone)
            intent.putExtra("productID", product.productID)
            intent.putExtra("CHAT_IMG_KEY",product.images[0])

            FirebaseReferences.chatRef.whereEqualTo("storeID", product.storeID)
                .whereEqualTo("userID", UserInfo.userID).get().addOnSuccessListener { values->
                if (values.count() == 0) {
                    var chat = Chat(
                        ref.id,
                        UserInfo.userID.toString(),
                        UserInfo.name,
                        product.storeID,
                        arrayListOf(productId)
                    )
                    ref.set(chat).addOnSuccessListener {
                        intent.putExtra("chatID",ref.id)
                        binding.root.context.startActivity(intent)
                    }
                }
                    else if (values.count()==1){
                        val chat:Chat=values.first().toObject()
                        intent.putExtra("chatID",chat.chatID)
                       binding.root.context.startActivity(intent)

                }
                    else{
                        Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()
                    }

            }
//                    if (firstTime) {
//            ref.set(chat).addOnSuccessListener {
//
//                FirebaseReferences.chatRef.document(ref.id).collection("messages")
//                    .document(messageID)
//                    .set(message)
//                messageText.clear()
//            }
//           firstTime=false
//        }
//        else{
//            FirebaseReferences.chatRef.document(ref.id).collection("messages")
//                .document(messageID)
//                .set(message)
//            messageText.clear()
//        }


        }
        binding.imgLocation.setOnClickListener {
            val location: Location = storeInfo.locations!![0]
            var intent: Intent = Intent(binding.root.context, MapsActivity::class.java)
            intent.putExtra("locationLat", location.latitude)
            intent.putExtra("locationLang", location.longitude)
            intent.putExtra("storeName", product.storeName)
            startActivityForResult(intent, MAPS_CODE)


        }

        binding.btnBuyProduct.setOnClickListener {
            var specialDiscount: SpecialDiscount = SpecialDiscount(10F, DiscountType.Fixed)

            var productCart: ProductCart = ProductCart(product,3,specialDiscount, 20)
            //productCart.quantity = 3
            //productCart.specialDiscount = specialDiscount

            var checkout: Checkout = Checkout(DeliveryMethod.Door, PaymentMethod.Fawry, User.userLocation, product.price, 12)
            checkout.addProduct(productCart)

            for (product in checkout.getAllProducts()){
                var order: Order = Order(product, checkout, OrderStatus.Current)
                ordersNM.addOrder(order)
            }
        }


//        Toast.makeText(context,productId,Toast.LENGTH_SHORT).show()

        //imgSlider
//        imageList.add(SlideModel("https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70"))
//        imageList.add(SlideModel("https://cdn.cliqueinc.com/posts/285870/best-cheap-spring-accessories-285870-1583111706473-main.750x0c.jpg?interlace=true&quality=70"))
//        imageList.add(SlideModel("https://i.pinimg.com/236x/35/11/21/351121d0c57db7df186885dc077f7323.jpg"))

        //property recycler
//        val property = ArrayList<Property>()
//        property.add(Property("select Size", arrayListOf("37", "38", "39", "40", "41")))


        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAPS_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val location: Parcelable? = data?.getParcelableExtra("Loc")
                if (location != null) {

                }
            }
        }
    }



}