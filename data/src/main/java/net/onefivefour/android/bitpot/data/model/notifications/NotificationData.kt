package net.onefivefour.android.bitpot.data.model.notifications

/**
 * An abstract class for the data of a notification.
 * Extend from this class to make use of automatic deserialization of an incoming notification.
 * The 'data' field of the incoming notification is then used as actual payload of it.
 * For more details see [BitpotMessagingService#createGson()] in the app module as
 * well as the RuntimeTypeAdapterFactory used in the koin [appModule]
 */
abstract class NotificationData