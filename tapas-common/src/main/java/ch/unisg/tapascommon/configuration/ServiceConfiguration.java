package ch.unisg.tapascommon.configuration;

public class ServiceConfiguration {

    public static final boolean IS_LOCALHOST = false;

    public static final String GROUP_NAME = "tapas-group4";
    public static final int GROUP_NUMBER = 4;

    public static final String LOCALHOST_TASKS = "http://127.0.0.1:8081";
    public static final String LOCALHOST_ROSTER = "http://127.0.0.1:8082";
    public static final String LOCALHOST_EXECUTOR_POOL = "http://127.0.0.1:8083";
    public static final String LOCALHOST_EXECUTOR_COMPUTATION = "http://127.0.0.1:8084";
    public static final String LOCALHOST_EXECUTOR_BIGROBOT = "http://127.0.0.1:8085";
    public static final String LOCALHOST_EXECUTOR_TEMPERATURE = "http://127.0.0.1:8087";
    public static final String LOCALHOST_AUCTION_HOUSE = "http://127.0.0.1:8086";

    public static final String PUBLIC_TASKS = "https://tapas-tasks.86-119-35-199.nip.io";
    public static final String PUBLIC_ROSTER = "https://tapas-roster.86-119-35-199.nip.io";
    public static final String PUBLIC_EXECUTOR_POOL = "https://tapas-executor-pool.86-119-35-199.nip.io";
    public static final String PUBLIC_EXECUTOR_COMPUTATION = "https://tapas-executor-computation.86-119-35-199.nip.io";
    public static final String PUBLIC_EXECUTOR_BIGROBOT = "https://tapas-executor-bigrobot.86-119-35-199.nip.io";
    public static final String PUBLIC_EXECUTOR_TEMPERATURE = "https://tapas-executor-temperature.86-119-35-199.nip.io";
    public static final String PUBLIC_AUCTION_HOUSE = "https://tapas-auction-house.86-119-35-199.nip.io";

    public static String getTaskListAddress() {
        return IS_LOCALHOST ? LOCALHOST_TASKS : PUBLIC_TASKS;
    }

    public static String getRosterAddress() {
        return IS_LOCALHOST ? LOCALHOST_ROSTER : PUBLIC_ROSTER;
    }

    public static String getExecutorPoolAddress() {
        return IS_LOCALHOST ? LOCALHOST_EXECUTOR_POOL : PUBLIC_EXECUTOR_POOL;
    }

    public static String getExecutorComputationAddress() {
        return IS_LOCALHOST ? LOCALHOST_EXECUTOR_COMPUTATION : PUBLIC_EXECUTOR_COMPUTATION;
    }

    public static String getExecutorBigrobotAddress() {
        return IS_LOCALHOST ? LOCALHOST_EXECUTOR_BIGROBOT : PUBLIC_EXECUTOR_BIGROBOT;
    }

    public static String getExecutorTemperatureAddress() {
        return IS_LOCALHOST ? LOCALHOST_EXECUTOR_TEMPERATURE : PUBLIC_EXECUTOR_TEMPERATURE;
    }

    public static String getAuctionHouseAddress() {
        return IS_LOCALHOST ? LOCALHOST_AUCTION_HOUSE : PUBLIC_AUCTION_HOUSE;
    }

    public static String getExecutorAddress(String executorId) {
        switch (executorId) {
            case "computation":
                return getExecutorComputationAddress();
            case "bigrobot":
                return getExecutorBigrobotAddress();
            case "temperature":
                return getExecutorTemperatureAddress();
        }
        return "";
    }
}
