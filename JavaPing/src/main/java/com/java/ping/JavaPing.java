package com.java.ping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Class for executing ping command using ProcessBuilder
 */
public class JavaPing{
    private static final Logger log = Logger.getLogger(JavaPing.class.getName());
    
    {
        BasicConfigurator.configure();
    }
	private final String LOG_PFX = "JavaPing :: " ;

	/**
     * Execute the operation
     * @param params Parameters needed: hostname and timeout
     * @return an object with the result
     */
    public Object execute(Map<String,String> params) 
    {
		
		Map<String,String> responseMap=new HashMap<String,String>();
		log.debug(LOG_PFX + " Method :: execute :: methodParameters :: " + params);
		
		String hostname=params.get("hostname")+"";
		int timeout=validateTimeoutValue(params.get("timeout")+"");
		ArrayList<String> argsList=new ArrayList<String>();
		
		try {
				argsList.add("ping");
				argsList.add(hostname);
				argsList.add("-W "+timeout);
				argsList.add("-c 3");
				responseMap = pingURL(argsList);
			
		}catch(Exception e) {
			log.error(LOG_PFX + " Error executing method :: Exception :: " + e);
			responseMap.put("PING_STATUS", "Error");		
		}
		return responseMap;
	}

	/**
	 * Execute ping 
	 * @param args arguments for pig command
	 * @throws Exception
	 */
    private Map<String, String> pingURL(ArrayList<String> args)  throws Exception 
    { 
    	Map<String, String> result = new HashMap<String, String>();
    	log.debug(LOG_PFX + "Method :: pingURL :: Executing method :: " + args);
    	
        // creating the sub process, execute system command 
        ProcessBuilder build = new ProcessBuilder(args); 
        Process process = build.start(); 
          
        // BufferedReader to read the output 
        BufferedReader input = new BufferedReader(new InputStreamReader 
                                   (process.getInputStream())); 
        String s = null; 
        ArrayList<String> response=new ArrayList<String>();
        
        //reading the command output
        while((s = input.readLine()) != null) 
        { 
        	log.debug(LOG_PFX + "Method :: pingURL :: Executing :: " + s);
            if(s.contains("packets")) {
            	response.add(s);
            }
        } 
        
        //Verify the response
        if(response.isEmpty()){
        	log.debug(LOG_PFX + " IP Address is not reachable / Unknown host:: Exception :: ");
			result.put("PING_STATUS", "Error");	
        	return result;        	
        }
        
        String[] temp=response.get(0).split(",");
        log.debug(LOG_PFX + "Method :: pingURL :: There are"+temp[1]);
        
        if(temp[1].contains("0")) {
        	log.debug(LOG_PFX + " IP Address is not reachable");
			result.put("PING_STATUS", "Failure");
        }else {
        	log.debug(LOG_PFX + " IP Address is reachable");
			result.put("PING_STATUS", "Success");
        }
        return result;
    } 
	

	/**
	 * validateTimeoutValue
	 * @param timoutValue
	 * @return timeout
	 */
	private int validateTimeoutValue(String timoutValue){
		int timeout=20;
		try{
			timeout = Integer.valueOf(timoutValue);
			
		}catch(Exception e){
			//Exception while reading \"ping.dsa.timeout\" environment property
			timeout=20;
		}
		return timeout;
	}
	

}