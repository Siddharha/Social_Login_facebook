package myapp.sociallogin.fragments;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import myapp.sociallogin.R;

/**
 * Created by BLUEHORSE 123 on 6/20/2016.
 */
public class LoginFragment extends Fragment {
    private View rootView;
    private LoginButton loginButton;
    CallbackManager callbackManager;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        rootView = inflater.inflate(R.layout.fragment_social, container, false);
        initialize();
        onClick();
        return rootView;
    }

    private void initialize() {
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
       /* try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.facebook.samples.loginhowto",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/

    }

    private void onClick() {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //Toast.makeText(getActivity(),loginResult.toString(),Toast.LENGTH_SHORT).show();
            String id = loginResult.getAccessToken().getUserId();
                Log.e("id",id);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(),"Canceled!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity(),exception.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
