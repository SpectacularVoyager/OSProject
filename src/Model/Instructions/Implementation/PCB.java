package Model.Instructions.Implementation;

import lombok.ToString;

@ToString
public class PCB {
    int job, time, lines;

    public PCB(int job, int totalTime, int totalLines) {
        this.job = job;
        this.totalTime = totalTime;
        this.totalLines = totalLines;
        time = 0;
        totalLines = 0;
    }

    int totalTime, totalLines;
}
