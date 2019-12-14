package main;

import exceptions.CompressException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import java.awt.*;

import java.io.IOException;

public class Frame extends JFrame {
    private JButton compressButton;
    private JButton extractButton;
    private JTextArea extractTextArea;
    private JTextArea compressTextArea;
	private JScrollBar scrollBar;
	private JProgressBar progressBar;
    private JFrame jfrm;

    public Frame(String name) {
		super(name);
		jfrm = this;
		init();
    }

    public void init() {
		jfrm.setLayout(new FlowLayout());
		jfrm.setSize(300, 200);
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		compressButton = new JButton("Сжать");
		extractButton = new JButton("Извлечь");


		compressTextArea = new JTextArea(1, 20); 
		compressTextArea.setEditable(true);
		extractTextArea = new JTextArea(1, 20);
		extractTextArea.setEditable(true);

		scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollBar.add(compressTextArea);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		compressButton.addActionListener(ae -> {
            progressBar.setValue(0);
            try {
               	String s = "";
            	for (String line : Runner.startProc("compress", compressTextArea.getText())) {
               		if (line != null)
               			s += line + '\n';
            		else {
                   		JOptionPane.showMessageDialog(jfrm, s);
          			}
       			}
       			progressBar.setValue(100);
       			JOptionPane.showMessageDialog(jfrm, s);
       		} catch (IOException e) {
       			JOptionPane.showMessageDialog(jfrm, "Некорректный ввод");
       		} catch (NullPointerException ignore) {
           		ignore.printStackTrace();
       		} catch (CompressException ce) {
            	JOptionPane.showMessageDialog(jfrm, ce.getMessage());
			}
		});

		extractButton.addActionListener(ae -> {
			progressBar.setValue(0);
			try {
				String s = "";
				for (String line : Runner.startProc("extract", extractTextArea.getText())) {
					if (line != null)
						s += line + '\n';
					else {
						JOptionPane.showMessageDialog(jfrm, s);
					}
				}
				JOptionPane.showMessageDialog(jfrm, s);
			} catch (CompressException ce) {
				JOptionPane.showMessageDialog(jfrm, ce.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});	
		 
		jfrm.add(compressTextArea);
		jfrm.add(compressButton);
		jfrm.add(extractTextArea);
		jfrm.add(extractButton);
		jfrm.add(progressBar);
		jfrm.add(scrollBar);
		jfrm.setLocationRelativeTo(null);
        jfrm.setVisible(true);
    }
}
