package trabalhofinal;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.util.List;
import java.util.LinkedList;

public class jgroups extends ReceiverAdapter {
	public static int flag=0;
    JChannel channel;
    
    String user_name=System.getProperty("user.name", "n/a");
    public List<String> state=new LinkedList<String>();
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        String line= ""+msg.getObject();
        System.out.println(line);
        //System.out.println(state);
        //jgroups.flag=1;
       
        synchronized(state) {
            state.add(line);
        }
    }

    public byte[] getState() {
        synchronized(state) {
            try {
                return Util.objectToByteBuffer(state);
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
	public void setState(byte[] new_state) {
        try {
            List<String> list=(List<String>)Util.objectFromByteBuffer(new_state);
            synchronized(state) {
                state.clear();
                state.addAll(list);
            }
            System.out.println("received state (" + list.size() + " messages in chat history):");
            for(String str: list) {
                System.out.println(str);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        channel=new JChannel();
        channel.setReceiver(this);
        channel.connect("nuvem");
        channel.getState(null, 10000);
        eventLoop();
        //channel.close();
    }

    private void eventLoop() {
    	int cont = 0;
    	int last=0;
    	int cont_s;
    	String msg_recebida[], usuario, senha;
    	
    	 while(true) {
    		 last = state.size();  
    		 cont_s=0;
    		 System.out.flush();
    		 if(last != 0){ 
    			 msg_recebida = state.get(last-1).split(":");
    			 cont_s = Integer.parseInt(msg_recebida[2]);
        		 if (cont_s == cont){
        			  usuario = msg_recebida[0];
                      senha = msg_recebida[1];

                      System.out.println("usuario  "+ usuario);
                      System.out.println("senha  "+ senha);
                      Nuvem.novoUsuario(usuario,senha);
                      
                      for (int x = 0; x < Nuvem.nos.size(); x++) {
                          System.out.println("***No; " + Nuvem.nos.get(x).getIdAux());
                          System.out.println("-- " + Nuvem.nos.get(x).getID() + " " + Nuvem.nos.get(x).getAdress());
                          System.out.println("-->" + Nuvem.nos.get(x).getNo().printFingerTable());
                          System.out.println("->" + Nuvem.nos.get(x).getNo().printEntries());
                          System.out.println("\n");
                       }
                      cont++;
        		 }
        		
    		 }
    		 
    	 }
        /*BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); 
                System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
               
                //System.out.println("usuario"+ usuario);

                //System.out.println("senha"+ senha);
                Nuvem.novoUsuario(usuario,senha);
                
                
                
                for (int x = 0; x < Nuvem.nos.size(); x++) {
                            System.out.println("***No; " + Nuvem.nos.get(x).getIdAux());
                            System.out.println("-- " + Nuvem.nos.get(x).getID() + " " + Nuvem.nos.get(x).getAdress());
                            System.out.println("-->" + Nuvem.nos.get(x).getNo().printFingerTable());
                            System.out.println("->" + Nuvem.nos.get(x).getNo().printEntries());
                            System.out.println("\n");
                }
               // System.out.println("aqui foi"+msg.toString());
                
               
                
            }
            catch(Exception e) {
            }
        }*/
    }
}

