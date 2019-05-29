package me.xethh.libs.mtt.MessageTransferTool.client;

import me.xethh.libs.mtt.MessageTransferTool.base.MDCActor;
import org.apache.commons.net.telnet.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.util.HashMap;
import java.util.Map;

public class Client extends MDCActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TelnetClientTask.class, task->{
                    TelnetClient tc = new TelnetClient();
                    boolean hasError = false;

                    TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
                    EchoOptionHandler echoopt = new EchoOptionHandler(true, false, true, false);
                    SuppressGAOptionHandler gaopt = new SuppressGAOptionHandler(true, true, true, true);

                    Map<String, String> map = new HashMap<>();
                    map.put("type","Telnet");
                    map.put("template",task.getTemplate());
                    map.put("targetType",task.getTargetType());
                    map.put("hostFrom", Inet4Address.getLocalHost().getHostAddress());
                    map.put("hostTo",task.getHost());
                    map.put("portTo",task.getPort()+"");
                    try
                    {
                        tc.addOptionHandler(ttopt);
                        tc.addOptionHandler(echoopt);
                        tc.addOptionHandler(gaopt);
                    }
                    catch (InvalidTelnetOptionException e)
                    {
                        log.error("Error registering option handlers: " + e.getMessage());
                        map.put("error",e.getMessage());
                        hasError=true;
                    }

                    try
                    {
                        tc.connect(task.getHost(), task.getPort());

                        try
                        {
                            tc.disconnect();
                        }
                        catch (IOException e)
                        {
                            log.error("Exception while connecting:" + e.getMessage());
                            map.put("error",e.getMessage());
                            hasError=true;
                        }
                    }
                    catch (IOException e)
                    {
                        log.error("Exception while connecting:" + e.getMessage());
                        map.put("error",e.getMessage());
                        hasError=true;
                    }
                    if(hasError){
                        
                    }
                })
                .build();
    }
}
