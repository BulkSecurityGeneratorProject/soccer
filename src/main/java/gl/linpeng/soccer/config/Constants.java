package gl.linpeng.soccer.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_SWAGGER = "swagger";
    // Spring profile used to disable running liquibase
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String SOCCER_STATUS_OK = "OK";
    public static final String SOCCER_STATUS_INACTIVE = "INACTIVE";
    public static final String SOCCER_STATUS_CANCEL = "CANCEL";

    private Constants() {
    }

    /**
     * Soccer Event type:
     * first level code include: association(10),division(20),division event(30),club(40),team(50),player(60),coach(70),referee(80)
     * second level code include: association(01),division(02),division event(03),club(04),team(05),player(06),coach(07),referee(08)
     * third level code include: create(01),update(02),cancel(03),transfer(转会04),loan(租借05),renew(续约06),league prize(联赛奖项20),cup prize(杯赛奖项30)
     */
    public enum SoccerEventType {
        ASSOCIATION_CREATE(100101, "协会成立"), ASSOCIATION_INACTIVE(100102, "协会不活动"), ASSOCIATION_CANCEL(100103, "协会注销"),
        DIVISION_CREATE(100201, "赛事创建"), DIVISION_INACTIVE(100202, "赛事暂停"), DIVISION_CANCEL(100203, "赛事注销"),
        DIVISION_EVENT_CREATE(103001, "赛事举办"), DIVISION_EVENT_INACTIVE(103002, "赛事停办"), DIVISION_EVENT_CANCEL(103003, "赛事取消"),
        CLUB_CREATE(104001, "俱乐部注册"), CLUB_INACTIVE(104002, "俱乐部不活动"), CLUB_CANCEL(104003, "俱乐部注销"), CLUB_CHANGE_VENUE(104004, "更换主场"),
        TEAM_CREATE(105001, "球队注册"), TEAM_INACTIVE(105002, "球队不活动"), TEAM_CANCEL(105003, "球队注销"), TEAM_JOIN_DIVISION_EVENT(500301, "球队参加赛事"), TEAM_UPDATE_DIVISION_EVENT(500302, "球队更新赛事"), TEAM_LEAVE_DIVISION_EVENT(500303, "球队离开赛事"),
        PLAYER_CREATE(106001, "球员注册"), PLAYER_INACTIVE(106002, "球员不活动"), PLAYER_CANCEL(106003, "球员注销"), PLAYER_ACCESSION(600501, "球员入队"), PLAYER_DIMISSION(600503, "球员离队"), PLAYER_TRANSFER(600504, "球员转会"), PLAYER_LOAN(600505, "球员租借"),
        COACH_CREATE(107001, "教练员注册"), COACH_INACTIVE(107002, "教练员不活动"), COACH_CANCEL(107003, "教练员注销"), COACH_ACCESSION(700501, "教练员上任"), COACH_DIMISSION(700503, "教练员下课"), COACH_TRANSFER(700504, "教练员转会"),
        REFEREE_CREATE(108001, "裁判注册"), REFEREE_INACTIVE(108002, "裁判不活动"), REFEREE_CANCEL(108003, "裁判注销");
        private int value;
        private String title;

        SoccerEventType(int value, String title) {
            this.title = title;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
