package com.landfilleforms.android.landfille_forms.Warmspot;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by owchr on 2/15/2017.
 */

//TODO: Integrate sqlitedb onto warmspot list and add/remove/update
public class WarmspotList {

    private static WarmspotList sWarmspotData;
    private List<WarmspotData> mWarmspotDatas;

    public static WarmspotList get(Context context){
       if (sWarmspotData == null) {
           sWarmspotData = new WarmspotList(context);
       }
        return sWarmspotData;
    }

    public WarmspotList(Context context) {

    }

    public List<WarmspotData> getWarmspotData() {
        return mWarmspotDatas;
    }

//    public WarmspotData getDataForWarmspots(UUID id) {
//        for (WarmspotData w: mWarmspotDatas) {
//            if (w.getId().equals(id)) {
//                return w;
//            }
//            return null;
//        }
//        //return mWarmspotDatas;
//    }


}
