package com.example.tablia.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import io.reactivex.rxjava3.core.Observable;

public class NetworkUtil {

    public static Observable<Boolean> observeNetwork(Context context) {

        return Observable.create(emitter -> {

            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (cm == null) {
                emitter.onNext(false);
                emitter.onComplete();
                return;
            }

            emitter.onNext(isConnected(cm));

            ConnectivityManager.NetworkCallback callback =
                    new ConnectivityManager.NetworkCallback() {

                        @Override
                        public void onAvailable(Network network) {
                            emitter.onNext(true);
                        }

                        @Override
                        public void onLost(Network network) {
                            emitter.onNext(false);
                        }

                        @Override
                        public void onCapabilitiesChanged(
                                Network network,
                                NetworkCapabilities networkCapabilities) {

                            boolean hasInternet =
                                    networkCapabilities.hasCapability(
                                            NetworkCapabilities.NET_CAPABILITY_INTERNET)
                                            && networkCapabilities.hasCapability(
                                            NetworkCapabilities.NET_CAPABILITY_VALIDATED);

                            emitter.onNext(hasInternet);
                        }
                    };

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cm.registerDefaultNetworkCallback(callback);
            } else {
                NetworkRequest request = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build();

                cm.registerNetworkCallback(request, callback);
            }

            emitter.setCancellable(() -> cm.unregisterNetworkCallback(callback));
        });
    }

    private static boolean isConnected(ConnectivityManager cm) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = cm.getActiveNetwork();
            if (network == null) return false;

            NetworkCapabilities caps = cm.getNetworkCapabilities(network);
            if (caps == null) return false;

            return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }

        return false;
    }
}