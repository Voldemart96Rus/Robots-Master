package gui;

import java.awt.BorderLayout;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    public Observer gameINFO;
    private final GameVisualizer m_visualizer;
    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public RobotInfo createGameInfo(Object_Window gameInfo)
    {
        RobotInfo robot = new RobotInfo();
        robot.setSize(gameInfo.W, gameInfo.H);
        robot.setName("Информер игры");
        robot.setLocation(gameInfo.X, gameInfo.Y);
        register(robot);
        return robot;
    }

    private void register(Observer robot) {
        gameINFO = robot;
        m_visualizer.register(robot);
    }
}
