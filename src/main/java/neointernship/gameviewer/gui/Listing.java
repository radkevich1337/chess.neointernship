package neointernship.gameviewer.gui;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс для листания стрелочками
 */
public class Listing {
    private final JFrame frame;
    /**
     * кнопка
     */
    private final JButton buttonForward;
    private final JButton buttonBackward;

    private final String name = "История";
    /**
     * выводит сообщение
     */
    /**
     * считывает сообщение
     */

    public Listing() {
        frame = new JFrame(name);
        buttonForward = new JButton("Вперед");
        buttonBackward = new JButton("Назад");
        buttonForward.setBounds(180, 95, 95, 32);

        frame.add(buttonForward);
        frame.setBounds(500,250,320,175);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public int getLobbyIndex() {
        return 0;
    }

    public String getAnswer() throws InterruptedException {
        final String[] answer = new String[1];
        List<Integer> holder = new LinkedList<Integer>();

        buttonForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                synchronized (holder) {
                    holder.add(0);
                    holder.notify();
                }
                answer[0] = "Forward";
            }
        });
        buttonBackward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                synchronized (holder) {
                    holder.add(0);
                    holder.notify();
                }
                answer[0] = "Backward";
            }
        });

        synchronized (holder) {
            while (holder.isEmpty())
                holder.wait();
            holder.remove(0);
        }

        return answer[0];
    }
}
