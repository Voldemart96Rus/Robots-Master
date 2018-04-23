package gui;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class RobotsMaser  extends Observable {
    //Позиция робота
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    //направление робота
    private volatile double m_robotDirection = 0;
    //координаты цели по умолчанию(во время запуска он едет немного, чтобы остановиться))))
    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    //максимальная скорость
    private static final double maxVelocity = 0.1;
    //максимальная угловая скорость
    private static final double maxAngularVelocity = 0.001;

    public RobotsMaser(){

    }

    public double getRobotPositionX(){
        return m_robotPositionX;
    }
    public double getRobotPositionY(){
        return m_robotPositionY;
    }
    public int getTargetPositionX(){
        return m_targetPositionX;
    }
    public int getTargetPositionY(){
        return m_targetPositionY;
    }
    public double getRobotDirection(){
        return m_robotDirection;
    }

    private List<Observer> position = new ArrayList<Observer>();

    public void addNews(String newsItem) {

        for (Observer outlet : this.position) {
            outlet.update(this, newsItem);
        }
    }
        public void register(Observer outlet) {
        position.add(outlet);
    }

    //устангановить позицию цели
    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
    //перерасчет дистанции до цели
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    // угол к цели
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    //событие обновление модели
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, 10);
    }
    //применить ограничения
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    //перемещение робата
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
        addNews( "Координаты работа ("+(int)m_robotPositionX+":"+ (int)m_robotPositionY+")\n");
    }
    //как нормализованные радианы
    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }


}
