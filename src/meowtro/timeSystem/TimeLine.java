package meowtro.timeSystem;
public class TimeLine{
    private static TimeLine timeLine = new TimeLine();
    private int year, month, day, hour, minute, second, timeUnit;
    private long totalSeconds, totalTimeUnit;
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
        this.totalSeconds = 0;
        this.totalTimeUnit = 0;
        this.year = 0;
        setCalender();
    }
    private void setCalender(){
        long totalMinutes = this.totalSeconds/60;
        this.second = (int) (this.totalSeconds-totalMinutes*60);
        
        long totalHours = totalMinutes / 60;
        this.minute = (int) (totalMinutes-totalHours*60);
        
        long totalDays = totalHours / 24;
        this.hour = (int) (totalHours-totalDays*24);

        long totalMonths = totalDays / 30;
        this.day = (int) (totalDays-totalMonths*30);

        long totalYears = totalMonths / 12;
        this.month = (int) (totalMonths-totalYears*12);
        this.year = (int) totalYears;
        // if(this.month>=12){
        //     reset();
        // }
    }

    public void update(){
        this.totalSeconds += this.timeUnit;
        this.totalTimeUnit += 1;
        setCalender();
    }
    public static TimeLine getInstance(){
        return timeLine; 
    }
    public static String convertSecondsToCalender(long totalSeconds){
        long totalMinutes = totalSeconds/60;
        int second = (int) (totalSeconds-totalMinutes*60);
        
        long totalHours = totalMinutes / 60;
        int minute = (int) (totalMinutes-totalHours*60);
        
        long totalDays = totalHours / 24;
        int hour = (int) (totalHours-totalDays*24);

        long totalMonths = totalDays / 30;
        int day = (int) (totalDays-totalMonths*30);

        long totalYears = totalMonths / 12;
        int month = (int) (totalMonths-totalYears*12);
        int year = (int) totalYears;
        return String.format("%02d-%02d-%02d %02d:%02d:%02d",
            year, month, day, hour, minute, second);
    }
    public static long convertCalenderToSeconds(String duration){
        // in YY-MM-DD HH:MM:SS format
        String[] fmtTime = duration.strip().split(" ");
        String[] YMD = fmtTime[0].split("-");
        String[] HMS = fmtTime[1].split(":"); 
        long year = Integer.parseInt(YMD[0]);
        long month = Integer.parseInt(YMD[1]);
        long day = Integer.parseInt(YMD[2]);

        long hour = Integer.parseInt(HMS[0]);
        long minute = Integer.parseInt(HMS[1]);
        long second = Integer.parseInt(HMS[2]);

        return ((((((year*12+month)*30+day)*24+hour)*60+minute)*60)+second);
        // return (((((((((((((((((Integer.parseInt(YMD[0])*12)+Integer.parseInt(YMD[1])*30)+Integer.parseInt(YMD[2])*24)+Integer.parseInt(HMS[0])*60)+Integer.parseInt(HMS[1])*60+Integer.parseInt(HMS[2]))))))))))))));

    }
    boolean greaterOrEqualThanTime(String duration){
        // in YY-MM-DD HH:MM:SS format
        long currentTotalSeconds = TimeLine.convertCalenderToSeconds(duration);
        if(currentTotalSeconds>=this.totalSeconds){
            return true;
        }
        return false;
    }
    boolean matchCalenderwoYear(String duration){
        String curCalender = getCalenderTime();
        if(duration.strip().equals(curCalender.split("-")[1])){
            return true;
        }
        return false;
    }
    public long getCurrentTotalTimeUnit(){
        return this.totalTimeUnit;
    }
    public String getCalenderTime(){
        return String.format("%02d-%02d-%02d %02d:%02d:%02d", this.year, this.month, this.day, this.hour, this.minute, this.second);
    }

    public static void main(String[] args){
        TimeLine t =  TimeLine.getInstance();

        String duration = TimeLine.convertSecondsToCalender(9876543210123L);
        long seconds = TimeLine.convertCalenderToSeconds(duration);

        System.out.printf("%s\n%d\n", duration, seconds);
    }

}