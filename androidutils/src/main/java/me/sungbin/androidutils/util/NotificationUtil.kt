import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build

@Suppress("DEPRECATION")
object NotificationUtil {
    fun createChannel(context: Context, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getManager(context).createNotificationChannelGroup(
                NotificationChannelGroup(
                    name,
                    name
                )
            )

            val channelMessage =
                NotificationChannel(name, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                    this.description = description
                    group = name
                    enableVibration(false)
                }
            getManager(context).createNotificationChannel(channelMessage)
        }
    }

    private fun getManager(context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNormalNotification(
        context: Context,
        id: Int,
        channelId: String,
        title: String,
        content: String,
        icon: Int,
        isOnGoing: Boolean
    ) {
        getManager(context).notify(
            id,
            getNormalNotification(context, channelId, title, content, icon, isOnGoing).build()
        )
    }

    fun showInboxStyleNotification(
        context: Context,
        id: Int,
        channelId: String,
        title: String,
        content: String,
        boxText: List<String>,
        icon: Int,
        isOnGoing: Boolean
    ) {
        var builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
        } else Notification.Builder(context)

        builder = builder.setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(icon)
            .setAutoCancel(true)
            .setOngoing(isOnGoing)

        val inboxStyle = Notification.InboxStyle()
        inboxStyle.setBigContentTitle(title)
        inboxStyle.setSummaryText(content)

        for (element in boxText) {
            inboxStyle.addLine(element)
        }

        builder.style = inboxStyle

        getManager(context).notify(id, builder.build())
    }

    fun getNormalNotification(
        context: Context,
        channelId: String,
        title: String,
        content: String,
        icon: Int,
        isOnGoing: Boolean
    ): Notification.Builder {
        var builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
        } else Notification.Builder(context)

        builder = builder.setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(icon)
            .setAutoCancel(true)
            .setOngoing(isOnGoing)

        return builder
    }
}
