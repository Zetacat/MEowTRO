package meowtro.timeSystem;
public class TimeLine{
    private static TimeLine timeLine = new TimeLine();
    private int month, day, hour, minute, second, timeUnit, runSpeed;
    private long totalTimeUnit;
    private TimeLine(){
        reset();
    }
    public void reset(){
        this.month = 0;
        this.day = 0;
        this.hour = 0;
        this.minute = 0;
        this.second = 0;
        this.timeUnit = 600; // seconds
        this.totalTimeUnit = 0;
        this.runSpeed = 1;
        setCalender();
    }
    private void setCalender(){
        long totalMinutes = this.totalTimeUnit/60;
        this.second = (int) (this.totalTimeUnit-totalMinutes*60);
        
        long totalHours = totalMinutes / 60;
        this.minute = (int) (totalMinutes-totalHours*60);
        
        long totalDays = totalHours / 24;
        this.hour = (int) (totalHours-totalDays*24);

        long totalMonths = totalDays / 30;
        this.day = (int) (totalDays-totalMonths*30);
        this.month = (int) totalMonths;
        if(this.month>=12){
            reset();
        }
    }
    public void update(){
        this.totalTimeUnit += this.timeUnit*this.runSpeed;
        setCalender();
    }
    public static TimeLine getInstance(){
        return timeLine; 
    }
    public void pauseTimeLine(){
        this.runSpeed = 0;
    }
    public void startTimeLine(){
        this.runSpeed = 1;
    }
    public void fastForwardTimeLine(){
        this.runSpeed = 2;
    }
    public long getCurrentTotalTimeUnit(){
        return this.totalTimeUnit;
    }
    public String getCalenderTime(){
        return String.format("%02dM %02dD %02d:%02d:%02d", this.month, this.day, this.hour, this.minute, this.second);
    }

}