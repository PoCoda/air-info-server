# air-info-server
Backend for https://github.com/tondi/air-info

demo: <a href="https://tondi.pl/powietrze" target="_blank">https://tondi.pl/powietrze</a>

## Running
```shell
$ ./mvnw spring-boot:run
```
## API endpoints
```java
private final String API_CURRENT = "/current";
private final String API_STREAK_MATCHING = "/streak-matching";
private final String API_STREAK_EXCEEDING = "/streak-exceeding";
private final String API_BEST_SINCE = "/best-since";
private final String API_WORST_SINCE = "/worst-since";
private final String API_THIS_WEEK_AVERAGE = "/this-week-average";
private final String API_LAST_WEEK_AVERAGE = "/last-week-average";
private final String API_WORST_DISTRICT = "/worst-district";
```

## Return types
> All response return types are defined in responses package -
> https://github.com/tondi/air-info-server/tree/master/src/main/java/com/tondi/airinfoserver/response
