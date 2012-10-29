package websiteschema.mpsegment.dict;

import websiteschema.mpsegment.conf.MPSegmentConfiguration;

import java.io.*;
import java.util.HashMap;

public class StopList {

    public StopList() {
        posStops = new int[20];
    }

    public void loadStopList(String s) {
        String encoding = MPSegmentConfiguration.getINSTANCE().getDefaultFileEncoding();
        try {
            int i = 0;

            BufferedReader bufferedreader = new BufferedReader(
                    new InputStreamReader(
                    StopList.class.getClassLoader().getResourceAsStream(s), encoding));
            String line;
            while ((line = bufferedreader.readLine()) != null) {
                i++;
                line = line.trim();
                if (line.length() >= 1 && !line.startsWith("#")) {
                    if (!line.equalsIgnoreCase("\\space")) {
                        stopWordHashMap.put(line, 2);
                    } else {
                        stopWordHashMap.put(" ", 2);
                    }
                }
            }
            bufferedreader.close();
            bufferedreader = null;
        } catch (Exception exception) {
            System.out.println((new StringBuilder()).append("[StopWord] exception:").append(exception.getMessage()).toString());
        }

        String posStopList = MPSegmentConfiguration.getINSTANCE().getStopPosList();
        posStopList = posStopList.trim();
        if (posStopList.length() > 0) {
            String as[] = posStopList.split(",");

            if (as.length > 0) {
                for (int k = 0; k < as.length; k++) {
                    int j = POSUtil.getPOSIndex(as[k]);
                    if (j > 0) {
                        posStops[numPosStop] = j;
                        numPosStop++;
                    }
                }

            }
        }
    }

    public static boolean isStopWord(String s) {
        boolean flag = false;
        if (stopWordHashMap.get(s) > 0) {
            flag = true;
        }
        return flag;
    }

    public static boolean isPosStopWord(int posIndex) {
        boolean flag = false;
        if (numPosStop > 0) {
            for (int j = 0; j < numPosStop; j++) {
                if (posIndex != posStops[j]) {
                    continue;
                }
                flag = true;
                break;
            }

        }
        return flag;
    }

    public static boolean isStopWord(String s, int posIndex) {
        boolean flag = false;
        if (numPosStop > 0) {
            for (int j = 0; j < numPosStop; j++) {
                if (posIndex != posStops[j]) {
                    continue;
                }
                flag = true;
                break;
            }

        }
        if (!flag && stopWordHashMap.get(s) > 0) {
            flag = true;
        }
        return flag;
    }
    private final static HashMap<String, Integer> stopWordHashMap = new HashMap<String, Integer>();
    private static int posStops[];
    private static int numPosStop = 0;
}
