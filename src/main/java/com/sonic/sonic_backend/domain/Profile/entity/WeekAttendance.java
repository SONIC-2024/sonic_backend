package com.sonic.sonic_backend.domain.Profile.entity;

import com.sonic.sonic_backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeekAttendance extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="week_attendance_id")
    private Long id;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean MON;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean TUE;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean WED;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean THU;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean FRI;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean SAT;
    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean SUN;

    //바꿔야 하는 필드의 개수가 각각 다르다보니 전부 다른 메소드로 분리함
    //통합할 수 있는 방법이 없을지?
    public void updateOnMon() {this.MON = true; this.TUE = false; this.WED = false;this.THU = false; this.FRI = false; this.SAT = false; this.SUN = false;}
    public void updateOnTue() {this.TUE = true; this.WED = false;this.THU = false; this.FRI = false; this.SAT = false; this.SUN = false;}
    public void updateOnWed() {this.WED = true; this.THU = false; this.FRI = false; this.SAT = false; this.SUN = false;}
    public void updateOnThu() {this.THU = true; this.FRI = false; this.SAT = false; this.SUN = false;}
    public void updateOnFri() {this.FRI = true; this.SAT = false; this.SUN = false;}
    public void updateOnSat() {this.SAT = true; this.SUN = false;}
    public void updateOnSun() {this.SUN = true;}
}
