package com.shoplex.shoplex.model.services


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shoplex.shoplex.R
import com.shoplex.shoplex.view.activities.HomeActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG =R.string.FireBaseMessagingService
    var NOTIFICATION_CHANNEL_ID =R.string.notchannelid.toString()
    val NOTIFICATION_ID = 100
    override fun onMessageReceived(remoteMessage: RemoteMessage) {


        if (remoteMessage.getNotification() !=null ) {

            if (remoteMessage.data.size > 0) {
                val title = remoteMessage.data[this.getString(R.string.title)]
                val body = remoteMessage.data[this.getString(R.string.body)]
                showNotification( applicationContext,title, body)
            } else {
                val title = remoteMessage.notification!!.title
                val body = remoteMessage.notification!!.body
                showNotification( applicationContext,title, body)
            }
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    @SuppressLint(R.string.RemoteViewLayout.toString())
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews? {
        val remoteViews = RemoteViews(
            applicationContext.getPackageName(),
            R.layout.notification
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(
            R.id.icon,
            R.drawable.buynow
        )

        return remoteViews
    }


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("token","New Token")
    }


//    fun showNotification(
//        context: Context,
//        title: String?,
//        message: String?
//    ) {
//        // Pass the intent to switch to the MainActivity
//        val intent = Intent(context, HomeActivity::class.java)
//        // Assign channel ID
//        val channel_id = "notification_channel"
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        val pendingIntent = PendingIntent.getActivity(
//            context, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//        val cancel = Intent("com.example.cancel")
//        val cancelP =
//            PendingIntent.getBroadcast(context, 0, cancel, PendingIntent.FLAG_CANCEL_CURRENT)
//
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//        var builder = NotificationCompat.Builder(
//            applicationContext,
//            channel_id
//        )
//            .setSmallIcon(R.drawable.buynow)
//            .setAutoCancel(true)
//            .setVibrate(
//                longArrayOf(
//                    1000, 1000, 1000,
//                    1000, 1000
//                )
//            )
//            .addAction(R.drawable.star_24,"Cancel",cancelP)
//            .addAction(R.drawable.ic_star,"Review",pendingIntent)
//            .setOnlyAlertOnce(true)
//           .setContentIntent(pendingIntent)
//
//        // A customized design for the notification can be
//        // set only for Android versions 4.1 and above. Thus
//        // condition for the same is checked here.
//        builder = if (Build.VERSION.SDK_INT
//            >= Build.VERSION_CODES.JELLY_BEAN
//        ) {
//            builder.setContent(
//                getCustomDesign(title!!, message!!)
//            )
//        } // If Android Version is lower than Jelly Beans,
//        else {
//            builder.setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.buynow)
//        }
//        // Create an object of NotificationManager class to
//        // notify the
//        // user of events that happen in the background.
//
//        val notificationManager = context.getSystemService(
//            Context.NOTIFICATION_SERVICE
//        ) as NotificationManager?
//        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT
//            >= Build.VERSION_CODES.O
//        ) {
//            val notificationChannel = NotificationChannel(
//                channel_id, "web_app",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager!!.createNotificationChannel(
//                notificationChannel
//            )
//        }
//        notificationManager!!.notify(0, builder.build())
//    }

    fun showNotification(
        context: Context,
        title: String?,
        message: String?
    ) {
        val ii: Intent
        ii = Intent(context, HomeActivity::class.java)
        ii.data = Uri.parse(context.getString(R.string.custom)+ System.currentTimeMillis())
        ii.action = context.getString(R.string.actionstring) + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pi =
            PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT)
        val cancel = Intent(context.getString(R.string.canceled))
        val cancelP =
            PendingIntent.getBroadcast(context, 0, cancel, PendingIntent.FLAG_CANCEL_CURRENT)

//       val actions[]= NotificationCompat.Action[1]
        val actions = arrayOfNulls<NotificationCompat.Action>(1)

        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.e("Notification", "Created in up to orio OS device");
            notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(R.drawable.buynow)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .addAction(R.drawable.star_24,context.getString(R.string.Cancel),cancelP)
                .addAction(R.drawable.ic_star,context.getString(R.string.Review),pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
               // notification.flags = Notification.FLAG_AUTO_CANCEL
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(NOTIFICATION_ID, notification)

        } else {
            notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.buynow)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    fun CancelNotification(ctx: Context, notifyId: Int) {
        val s = Context.NOTIFICATION_SERVICE
        val mNM = ctx.getSystemService(s) as NotificationManager
        mNM.cancel(notifyId)
    }



}