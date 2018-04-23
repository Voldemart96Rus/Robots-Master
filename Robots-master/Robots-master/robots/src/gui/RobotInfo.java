package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class RobotInfo extends JInternalFrame implements Observer  {

    private TextArea m_robotContent;

    public RobotInfo(){

        super("Информер игры", true, true, true, true);

        m_robotContent = new TextArea("");
        m_robotContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_robotContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
       // updateLogContent();
    }


    @Override
    public void update(Observable o, Object str) {
        m_robotContent.append((String)str);
    }
}
