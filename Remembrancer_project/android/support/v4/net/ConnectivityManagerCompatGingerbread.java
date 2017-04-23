package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;

class ConnectivityManagerCompatGingerbread {
    ConnectivityManagerCompatGingerbread() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case ViewDragHelper.STATE_IDLE /*0*/:
            case ViewDragHelper.STATE_SETTLING /*2*/:
            case ViewDragHelper.DIRECTION_ALL /*3*/:
            case ViewDragHelper.EDGE_TOP /*4*/:
            case MotionEventCompat.ACTION_POINTER_DOWN /*5*/:
            case MotionEventCompat.ACTION_POINTER_UP /*6*/:
                return true;
            case ViewDragHelper.STATE_DRAGGING /*1*/:
                return false;
            default:
                return true;
        }
    }
}
