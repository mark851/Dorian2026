package com.example.data.cloud

import android.util.Log
import com.example.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object CloudSyncManager {
    private const val TAG = "CloudSyncManager"
    // Public cloud-shared object ID for Dora Fashions catalog
    const val CLOUD_OBJECT_ID = "ff808181a09d98f701a0fdf4a7b165a0"
    const val CLOUD_API_URL = "https://api.restful-api.dev/objects/$CLOUD_OBJECT_ID"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    /**
     * Publishes the full product catalog to the shared cloud backend
     * so that all users across all devices and web browsers can see it in realtime.
     */
    suspend fun publishCatalogToCloud(products: List<Product>): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val root = JSONObject()
            root.put("name", "Dora Fashions Catalog")

            val dataObj = JSONObject()
            dataObj.put("updatedAt", System.currentTimeMillis())
            dataObj.put("totalProducts", products.size)

            val array = JSONArray()
            for (p in products) {
                val pObj = JSONObject()
                pObj.put("id", p.id)
                pObj.put("name", p.name)
                pObj.put("category", p.category)
                pObj.put("price", p.price)
                pObj.put("stock", p.stock)
                pObj.put("colors", p.colors)
                pObj.put("sizes", p.sizes)
                pObj.put("imageUrl", p.imageUrl)
                pObj.put("isFeatured", p.isFeatured)
                pObj.put("isNewArrival", p.isNewArrival)
                pObj.put("isBestSeller", p.isBestSeller)
                pObj.put("description", p.description)
                pObj.put("updatedAt", p.updatedAt)
                array.put(pObj)
            }
            dataObj.put("products", array)
            root.put("data", dataObj)

            val body = root.toString().toRequestBody(JSON_MEDIA_TYPE)
            val request = Request.Builder()
                .url(CLOUD_API_URL)
                .put(body)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Log.d(TAG, "Successfully published ${products.size} products to cloud")
                Result.success(products.size)
            } else {
                Log.w(TAG, "Cloud publish returned error: ${response.code}")
                Result.failure(Exception("Cloud server responded with code ${response.code}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error publishing catalog to cloud", e)
            Result.failure(e)
        }
    }

    /**
     * Fetches products from the cloud backend so any user receives newly posted products
     * from the admin.
     */
    suspend fun fetchCatalogFromCloud(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(CLOUD_API_URL)
                .get()
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Failed to fetch cloud catalog: ${response.code}"))
            }

            val bodyString = response.body?.string() ?: return@withContext Result.success(emptyList())
            val root = JSONObject(bodyString)
            val dataObj = root.optJSONObject("data") ?: return@withContext Result.success(emptyList())
            val array = dataObj.optJSONArray("products") ?: return@withContext Result.success(emptyList())

            val list = mutableListOf<Product>()
            for (i in 0 until array.length()) {
                val pObj = array.getJSONObject(i)
                list.add(
                    Product(
                        id = pObj.optLong("id", System.currentTimeMillis()),
                        name = pObj.optString("name", "Bead Item"),
                        category = pObj.optString("category", "Bag Beads"),
                        price = pObj.optLong("price", 15000L),
                        stock = pObj.optInt("stock", 10),
                        colors = pObj.optString("colors", "Multi"),
                        sizes = pObj.optString("sizes", "Standard"),
                        imageUrl = pObj.optString("imageUrl", ""),
                        isFeatured = pObj.optBoolean("isFeatured", false),
                        isNewArrival = pObj.optBoolean("isNewArrival", true),
                        isBestSeller = pObj.optBoolean("isBestSeller", false),
                        description = pObj.optString("description", ""),
                        updatedAt = pObj.optLong("updatedAt", System.currentTimeMillis())
                    )
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching catalog from cloud", e)
            Result.failure(e)
        }
    }
}
