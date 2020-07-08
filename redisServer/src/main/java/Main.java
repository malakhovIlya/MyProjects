import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.TreeMap;

class PromoteApp {
    private static int USERS = 20;

    public static void main(String[] args) throws InterruptedException {
        Jedis jedis = new Jedis();
        TreeMap<String, String> nickname = new TreeMap<>();

        for (int i = 0; i <= USERS; i++) {
            nickname.put("nickname#" + i, "" + i);
        }
        jedis.hmset("nickname", nickname);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (new Random().nextBoolean()) {
                        for (int i = USERS + 1; i <= USERS + 2; i++) {
                            nickname.put("nickname#" + i, "" + i);
                            jedis.hmset("nickname", nickname);
                            jedis.lpush("newNickname", String.valueOf(i));
                            System.out.println("Пользователь " + i + " новый пользователь");
                        }
                        USERS = USERS + 2;
                    }
                }
            }
        });
        thread.start();
        while (true) {
            Thread.sleep(4000);
            int promoteUserId = new Random().nextInt(USERS);
            jedis.rpush("promoteNickname", String.valueOf(promoteUserId));
            System.out.println("Пользователь " + promoteUserId + " оплатил платную услугу");
        }
    }
}

class DisplayApp {
    public static void main(String[] args) throws InterruptedException {

        Jedis jedis = new Jedis();
        int userId = Math.toIntExact(jedis.hlen("nickname"));
        while (true) {
            System.out.println("На главной странице показываем пользователя " +
                    jedis.hmget("nickname", "nickname#" + new Random().nextInt(userId)));
            Thread.sleep(2000);
            if (jedis.exists("promoteNickname")) {
                System.out.println("На главной странице показываем платного пользователя " + jedis.rpop("promoteNickname"));
            }
            if (jedis.exists("newNickname")) {
                System.out.println("На главной странице показываем нового пользователя " + jedis.lpop("newNickname"));
            }
            Thread.sleep(2000);
        }
    }
}

