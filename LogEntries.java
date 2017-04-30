import java.io.Serializable;

public class LogEntries implements Serializable{
  static final long serialVersionUID = 42;
  public long time;
  public String action;

  public LogEntries(long time,String action){
    this.time=time;
    this.action=action;
  }
}
