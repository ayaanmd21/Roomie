package com.grexoft.roomie.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VolleyHelper {

    private final Context activity;
    private final boolean showProgress;
    private String message = "";

    public VolleyHelper(Context activity) {

        this.activity = activity;
        showProgress = true;
        message = "";
    }

    public VolleyHelper(Context context, boolean showProgress) {
        this.activity = context;
        this.showProgress = showProgress;
        message = "";
    }

    public VolleyHelper(Context context, boolean showProgress, String messsage) {
        this.activity = context;
        this.showProgress = showProgress;
        this.message = messsage;
    }

    public void CallApiPost(final String url, final JSONObject obj, final VolleyCallback callback) {
        final ProgressDialog pd = new ProgressDialog(activity);
        //    if (!url.equals(Constants.contactPullUrl) && showProgress) {
//        if (showProgress) {
//            pd.setCancelable(false);
//            if (message.equals(""))
//                pd.setMessage(activity.getString(R.string.please_wait));
//            else
//                pd.setMessage(message);
//            pd.show();
//        }
        System.out.println("api url: " + url);
        System.out.println("api obj: " + obj);
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        if (showProgress && pd.isShowing()) {
//                            pd.dismiss();
//                        }
                        System.out.println("api response for " + url + ": " + response.toString());
                        //    Log.i("api response", response.toString());
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (showProgress && pd.isShowing()) {
//                    pd.dismiss();
//                }
//                //noinspection ThrowablePrintedToSystemOut
                System.out.println(error.toString());
                callback.onError(getErrorMessage(error));
//                callback.onError("error: " + error.toString());
            }
        });
        jsObjRequest.setShouldCache(false);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);

    }


    public void CallApiGet(String url, final VolleyCallback callback) {
        final ProgressDialog pd = new ProgressDialog(activity);
        //    if (!url.equals(Constants.contactPullUrl) && showProgress) {
//        if (showProgress) {
//            pd.setCancelable(false);
//            pd.setMessage(activity.getString(R.string.please_wait));
//            pd.show();
//        }

        System.out.println("api url: " + url);
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        if (showProgress && pd.isShowing()) {
//                            pd.dismiss();
//                        }
                        System.out.println("api response: " + response.toString());
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
//                        if (showProgress && pd.isShowing()) {
//                            pd.dismiss();
//                        }

                        callback.onError(getErrorMessage(error));
                        // callback.onError(error.toString());
                    }
                }
        );
        getRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);

    }


    public void CallApiGetArray(String url, final VolleyCallback callback) {
        System.out.println("api url: " + url);
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject json = new JSONObject();
                    json.put("array", response);
                    System.out.println(json.toString());
                    callback.onSuccess(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                System.out.println(error);
//                callback.onError(error.toString());
                callback.onError(getErrorMessage(error));

            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }

    public void CallApiGetString(String url, final VolleyStringCallback callback) {
        System.out.println("api url: " + url);
        final ProgressDialog pd = new ProgressDialog(activity);
//        if (!url.equals(Constants.contactPullUrl) && showProgress) {
//            pd.setCancelable(false);
//            pd.setMessage(activity.getString(R.string.please_wait));
//            pd.show();
//        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                if (showProgress && pd.isShowing()) {
//                    pd.dismiss();
//                }
                if (callback != null) {
                    callback.onSuccess(response);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (showProgress && pd.isShowing()) {
//                    pd.dismiss();
//                }
                callback.onError("error: " + error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private String getErrorMessage(VolleyError volleyError) {
        String msg = "error";
        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
            msg = "Connectivity Error!";

        } else if (volleyError instanceof AuthFailureError) {
            msg = "Authentication Error!";
        } else if (volleyError instanceof ServerError) {
            msg = "Something went wrong";
        } else if (volleyError instanceof NetworkError) {
            msg = "Network Error!";
        } else if (volleyError instanceof ParseError) {
            msg = "Something went wrong";
        }
        return msg;
    }

    public void CallApiPostArray(String url, final JSONArray obj, final VolleyCallback callback) {
        final ProgressDialog pd = new ProgressDialog(activity);
//        if (!url.equals(Constants.contactPullUrl) && showProgress) {
//            pd.setCancelable(false);
//            pd.setMessage(activity.getString(R.string.please_wait));
//            pd.show();
//        }
        System.out.println("api url: " + url);
        System.out.println("api obj: " + obj);
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        if (showProgress && pd.isShowing()) {
//                            pd.dismiss();
//                        }
                        try {
                            JSONObject json = new JSONObject();
                            json.put("array", response);
                            System.out.println("api response: " + json.toString());
                            callback.onSuccess(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (showProgress && pd.isShowing()) {
//                    pd.dismiss();
//                }
                callback.onError(getErrorMessage(error));
                //   callback.onError("error: " + error.toString());
            }
        });
        jsObjRequest.setShouldCache(false);
        //   if(url.c)
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);

    }

    public void CallApiPut(String url, final JSONObject obj, final VolleyCallback callback) {
        final ProgressDialog pd = new ProgressDialog(activity);
//        if (!url.equals(Constants.contactPullUrl) && showProgress) {
//            pd.setCancelable(false);
//            pd.setMessage(activity.getString(R.string.please_wait));
//            pd.show();
//        }
        System.out.println("api url: " + url);
        System.out.println("api obj: " + obj);
        RequestQueue queue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        if (showProgress && pd.isShowing()) {
//                            pd.dismiss();
//                        }
                        System.out.println("api response: " + response.toString());
                        callback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (showProgress && pd.isShowing()) {
//                    pd.dismiss();
//                }
//                //noinspection ThrowablePrintedToSystemOut
//                System.out.println(error);
//                callback.onError("error: " + error.toString());
                callback.onError(getErrorMessage(error));
            }
        });
        jsObjRequest.setShouldCache(false);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);

    }

    public void callApiGetImage(String url, final VolleyBitmapCallback callback) {
        System.out.println("api url: " + url);
        RequestQueue queue = Volley.newRequestQueue(activity);

        ImageRequest req = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                callback.onSuccess(response);

            }
        }, 512, 512, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(getErrorMessage(error));
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);

    }


    public interface VolleyCallback {
        void onSuccess(JSONObject result);

        void onError(String error);
    }


    public interface VolleyBitmapCallback {
        void onSuccess(Bitmap result);

        void onError(String error);
    }

    public interface VolleyStringCallback {
        void onSuccess(String result);

        void onError(String error);
    }


}