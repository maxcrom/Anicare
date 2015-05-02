package sep.software.anicare.service;

import java.io.IOException;
import java.net.MalformedURLException;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.AniCareProtocol;
import sep.software.anicare.event.AniCareException;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.model.AniCareUser;
import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.microsoft.windowsazure.mobileservices.ApiJsonOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import de.greenrobot.event.EventBus;
import sep.software.anicare.util.AsyncChainer;


public class AniCareServiceAzure implements AniCareService {

    private MobileServiceClient mobileClient;
    private final String AZURE_URL = "https://ani-care.azure-mobile.net/";
    private final String AZURE_KEY = "yHhHAGwAeqdZDFgMZkXYWVZEgQucFr12";
    private BlobStorageService mBlobStorageService;


    public AniCareServiceAzure(Context context) {
        RestClient.init();
        try {
            mobileClient = new MobileServiceClient(
                    AZURE_URL,
                    AZURE_KEY,
                    context);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mBlobStorageService = new BlobStorageService();
    }

    public void login(AniCareUser user, final EntityCallback<AniCareUser> callback) {
        Gson gson = new Gson();
        JsonObject userJson = gson.fromJson(gson.toJson(user), JsonObject.class);
        JsonObject jo = new JsonObject();
        jo.add("user", userJson);

        mobileClient.invokeApi("login", jo, new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                callback.onCompleted(new Gson().fromJson(arg0, AniCareUser.class));
            }
        });
    }

    public boolean isLoggedIn() {
        AniCareUser user = AniCareApp.getAppContext().getObjectPreference().getClass(AniCareUser.class);
        return user.getId() != null && !user.getId().equals("");
    }

    public void logout() {
        AniCareApp.getAppContext().getObjectPreference().removeClass(AniCareUser.class);
    }

    public void getGcmRegistrationId(final EntityCallback<String> callback) {
        (new AniCareAsyncTask<GoogleCloudMessaging, Void, String>() {

            @Override
            protected String doInBackground(GoogleCloudMessaging... params) {
                GoogleCloudMessaging gcm = params[0];
                try {
                    return gcm.register(AniCareProtocol.GCM_SENDER_ID);
                } catch (IOException e) {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                callback.onCompleted(result);
            }
        }).execute(GoogleCloudMessaging.getInstance(AniCareApp.getAppContext()));
    }

    public void sendMessage(AniCareMessage message, final EntityCallback<AniCareMessage> callback){

        mobileClient.invokeApi("send_message", new Gson().fromJson(message.toString(), JsonElement.class), new ApiJsonOperationCallback() {

            @Override
            public void onCompleted(JsonElement arg0, Exception arg1,
                                    ServiceFilterResponse arg2) {
                // TODO Auto-generated method stub
                if (arg1 == null) {
                    callback.onCompleted(new Gson().fromJson(arg0, AniCareMessage.class));
                } else {
                    EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.SERVER_ERROR));
                }

            }
        });
    }

    @Override
    public void uploadUserImage(String id, Bitmap image, final EntityCallback<String> callback) {
        mBlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_USER_PROFILE, id, image,
                new EntityCallback<String>() {

            @Override
            public void onCompleted(String entity) {
                callback.onCompleted(entity);
            }
        });
    }

    @Override
    public void uploadPetImage(String id, Bitmap image, final EntityCallback<String> callback) {
        mBlobStorageService.uploadBitmapAsync(BlobStorageService.CONTAINER_IMAGE, id, image,
                new EntityCallback<String>() {

            @Override
            public void onCompleted(String entity) {
                callback.onCompleted(entity);
            }
        });

    }

}