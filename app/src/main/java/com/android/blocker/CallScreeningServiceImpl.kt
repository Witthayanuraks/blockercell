package com.android.blocker

import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import android.content.Intent
import android.provider.ContactsContract
import android.telecom.Call.Details

class CallScreeningServiceImpl : CallScreeningService() {

    companion object {
        private const val TAG = "CallScreeningService"
    }

    override fun onScreenCall(callDetails: Call.Details) {
        Log.d(TAG, "onScreenCall triggered")
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: run {
            Log.w(TAG, "Phone number is null")
            return
        }
        Log.d(TAG, "Screening call from: $phoneNumber")

        if (!isNumberInContacts(phoneNumber)) {
            val response = CallResponse.Builder()
                .setDisallowCall(true)
                .setRejectCall(true)
                .setSkipCallLog(false)
                .setSkipNotification(true)
                .build()
            respondToCall(callDetails, response)
            val logMessage = "Blocked call from unknown number: $phoneNumber"
            Log.d(TAG, "Blocking call: $logMessage")
            sendLogToUI(logMessage)
        } else {
            val logMessage = "Allowed call from known number: $phoneNumber"
            Log.d(TAG, "Allowing call: $logMessage")
            sendLogToUI(logMessage)
        }
    }

    private fun sendLogToUI(logMessage: String) {
        Log.d(TAG, "Sending log to UI: $logMessage")
        val intent = Intent(MainActivity.ACTION_CALL_LOG)
        intent.putExtra("log_message", logMessage)
        sendBroadcast(intent)
    }

    private fun isNumberInContacts(number: String): Boolean {
        val contacts = mutableSetOf<String>()
        runCatching {
            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                null,
                null,
                null
            )?.use { cursor ->
                val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (cursor.moveToNext()) {
                    val contactNumber = cursor.getString(numberIndex)?.replace("[^0-9+]".toRegex(), "")
                    if (!contactNumber.isNullOrEmpty()) {
                        contacts.add(contactNumber)
                    }
                }
            }
        }.onFailure { e ->
            Log.e(TAG, "Error fetching contacts: ${e.message}")
        }

        val isInContacts = contacts.any { contact ->
            contact.endsWith(number) || number.endsWith(contact) || contact == number
        }
        Log.d(TAG, "Number $number in contacts: $isInContacts")
        return isInContacts
    }
}