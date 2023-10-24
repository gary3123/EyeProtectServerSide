package com.example.eyeprotext.missionList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class MisssionListConfig {
//    @Bean
//    CommandLineRunner commandLineRunner(MissionListRepository repository) {
//        return  args -> {
//            MissionList first = new MissionList.MissionListBuilder()
//                    .missionID( UUID.randomUUID())
//                    .title("使用專注模式")
//                    .progressType("分鐘")
//                    .progress(30).build();
//
//            MissionList second = new MissionList.MissionListBuilder()
//                    .missionID( UUID.randomUUID())
//                    .title("使用疲憊檢測")
//                    .progressType("次")
//                    .progress(1).build();
//
//            MissionList third = new MissionList.MissionListBuilder()
//                    .missionID( UUID.randomUUID())
//                    .title("使用藍光檢測器，並低於限制值")
//                    .progressType("次")
//                    .progress(1).build();
//
//            MissionList fourth = new MissionList.MissionListBuilder()
//                    .missionID( UUID.randomUUID())
//                    .title("在光線充足的地方使用專注模式")
//                    .progressType("次")
//                    .progress(1).build();
//
//            MissionList fifth = new MissionList.MissionListBuilder()
//                    .missionID( UUID.randomUUID())
//                    .title("進行護眼保健操")
//                    .progressType("次")
//                    .progress(1).build();
//
//            repository.saveAll(
//                    List.of(first, second, third, fourth, fifth)
//            );
//        };
//    }
}
