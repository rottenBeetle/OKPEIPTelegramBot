package com.rottenbeetle.newsletterokpeip.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "schedule")
@Getter
@Setter
public class Schedule implements Comparable<Schedule>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "groupname")
    private String group;
    @Column(name = "weekday")
    private String weekDay;
    @Type(type = "com.rottenbeetle.newsletterokpeip.model.arraymapping.CustomStringArrayType")
    @Column(name = "lessons", columnDefinition = "text[]")
    private String[] lessons ;

    public Schedule(String group, String weekDay, String[] lessons) {
        this.group = group;
        this.weekDay = weekDay;
        this.lessons = lessons;
    }

    public Schedule() {
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", group='" + group + '\'' +
                ", weekDay='" + weekDay + '\'' +
                ", lessons=" + Arrays.toString(lessons) +
                '}';
    }

    @Override
    public int compareTo(Schedule o) {
      return (int)(this.id - o.id);
    }
}
