/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icmpp;

import org.icmp4j.IcmpPingRequest;
import org.icmp4j.IcmpPingResponse;
import org.icmp4j.IcmpPingUtil;

/**
 *
 * @author LomitoFrito
 */
public class ICMPP
{
/*
    @Override
    public void start(Stage primaryStage)
    {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/

    public static void main(final String[] args) throws Exception
    {
        final IcmpPingRequest request = IcmpPingUtil.createIcmpPingRequest();
        request.setHost("www.google.org");

        for (int count = 1; count <= 4; count++)
        {
            final IcmpPingResponse response = IcmpPingUtil.executePingRequest(request);
            final String formattedResponse = IcmpPingUtil.formatResponse(response);
            System.out.println(formattedResponse);
            Thread.sleep(1000);
        }
    }

}
