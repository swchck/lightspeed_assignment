package com.lightspeed.lightspeed_assignment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Test {

    public record RecordE(String name, String release, String label, String ukRating, String usRating, String geRating) {
        public RecordE(String... values) {
            this(values[0], values[1], values[2], values[3], values[4], values[5]);
        }
    }

    public static void main(String[] args) {
        List<String> records = Arrays.asList("Name;2022/02/02;Label;1;1;1", "Name2;2022/02/02;Label2;2;1;-");
        List<RecordE> list = records.stream()
                .map(s -> s.split(";"))
                .map(RecordE::new)
                .toList();
        list.forEach(System.out::println);
        list
                .stream()
                .filter(r -> Objects.equals(r.ukRating, "1")
                        && Objects.equals(r.usRating, "1")
                        && Objects.equals(r.geRating, "1")
                )
                .forEach(System.out::println);
    }


    public enum Country {
        UK,
        US,
        GER
    }

    public record RecordEntry(String name, String release, String label, Map<Country, String> chartPositions) {
        public RecordEntry(String... values) {
            this(
                    values[0],
                    values[1],
                    values[2],
                    Map.of(Country.UK, values[3], Country.US, values[4], Country.GER, values[5])
            );
        }
    }

    public static void main(String[] args) {
        List<String> records = Arrays.asList("Name;2022/02/02;Label;1;1;1", "Name2;2022/02/02;Label2;2;1;-");
        List<RecordEntry> list = records.stream()
                .map(s -> s.split(";"))
                .map(RecordEntry::new)
                .toList();
        list.forEach(System.out::println);

        list.stream()
                .filter(recordEntry ->
                        recordEntry.chartPositions.values()
                                .stream().filter(v -> v.equals("1")).count() == 3
                )
                .forEach(System.out::println);
    }
}
