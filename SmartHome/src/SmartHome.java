import java.util.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonGroup;

public class SmartHome extends JFrame {
	
	private static final long serialVersionUID = 1L;

	SmartHomeControlHub controlHub;
	
	JPanel headerPanel;
	JLabel clock;
	JPanel outerPanel;
	JPanel controls;
	JPanel c_light;
	JPanel c_fan;
	JPanel c_air;
	JButton light;
	JRadioButton fan0;
	JRadioButton fan1;
	JRadioButton fan2;
	ButtonGroup fanButtons;
	JSlider aircon;
	JLabel ac_temp;
	JPanel devices;
	JPanel d_light;
	JLabel light_socket;
	ImageIcon light_off;
	ImageIcon light_on;
	JPanel d_fan;
	JLabel table_fan;
	ImageIcon fan_off;
	ImageIcon fan_low;
	ImageIcon fan_high;	
	JPanel d_air;
	JLabel ye_olde_window_ac;
	ImageIcon ac_off;
	ImageIcon ac_on;	
	
	String date;
	String time;
	String trigger;
	String resetTrigger = "Jan 1 1:00 a.m."; //Date / time format = MMM d h:mm a
	boolean resetFlag = true;
	
	SimpleDateFormat dateFormat;
	SimpleDateFormat timeFormat;
	SimpleDateFormat resetUpdateFormat;
	
	public SmartHome() {
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640,400);
		setLocation(1920,0);
		setTitle("SmartHome");
		setLayout(new BorderLayout());
		
		dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		timeFormat = new SimpleDateFormat("hh:mm:ss a");
		resetUpdateFormat = new SimpleDateFormat("MMM d h:mm a");

		//Create the UI, and split between controls and device status display
		headerPanel = new JPanel();
		clock = new JLabel();
		headerPanel.add(clock);
		outerPanel = new JPanel();
		outerPanel.setLayout(new GridLayout(1,2));
		//Controls
		controls = new JPanel();
		outerPanel.add(controls);
		controls.setBorder(new TitledBorder("App Controls"));
		controls.setLayout(new GridLayout(3,1));
		c_light = new JPanel();
		c_light.setBorder(new TitledBorder("Light"));
		c_fan = new JPanel();
		c_fan.setBorder(new TitledBorder("Fan Speed"));
		c_air = new JPanel();
		c_air.setBorder(new TitledBorder("Thermostat"));
		c_air.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		controls.add(c_light);
		controls.add(c_fan);
		controls.add(c_air);
		//Control element - Light Switch
		light = new JButton("Light"); //Default place holder text. Will update when the "controller" initializes.
		c_light.add(light);
		//control Element - Fan Speed Selector
		fan0 = new JRadioButton("Off");
		fan1 = new JRadioButton("Low");
		fan2 = new JRadioButton("High");
		fanButtons = new ButtonGroup();
		fan0.setSelected(true); //Default place holder text. Will update when the "controller" initializes.
		fanButtons.add(fan0);
		fanButtons.add(fan1);
		fanButtons.add(fan2);
		c_fan.add(fan0);
		c_fan.add(fan1);
		c_fan.add(fan2);
		//Control element - AC Thermostat
		aircon = new JSlider(0,25,0);
		aircon.setMajorTickSpacing(5);  
		aircon.setSnapToTicks(true);
		aircon.setPaintTicks(true);  
		aircon.setPaintLabels(true);
		ac_temp = new JLabel("\u00B0"+ "C");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		c_air.add(ac_temp, gbc); //Default place holder text. Will update when the "controller" initializes.
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		c_air.add(aircon,gbc);
		//Device status display
		devices = new JPanel();
		devices.setBorder(new TitledBorder("Simulated Home Hub Devices"));
		devices.setLayout(new GridLayout(3,1));
		//Device Status - Light
		d_light = new JPanel();
		light_off = new ImageIcon("src/LightOff.png");
		light_on = new ImageIcon("src/LightOn.png");
		light_socket = new JLabel();
		//Device Status - Fan
		d_fan = new JPanel();
		fan_off = new ImageIcon("src/FanOff.png");
		fan_low = new ImageIcon("src/FanLow.png");
		fan_high = new ImageIcon("src/FanHigh.png");
		table_fan = new JLabel();
		//Device Status - AC
		d_air = new JPanel();
		ac_off = new ImageIcon("src/ACOff.png");
		ac_on = new ImageIcon("src/ACOn.png");
		ye_olde_window_ac = new JLabel();
		
		//Initialize simulated controller object
		controlHub = new SmartHomeControlHub();
		
		//Simulate checking status for all devices... Will always be "off" as written here, but the status needs to come from the controller.
		initControls();

		d_light.add(light_socket);
		d_fan.add(table_fan);
		d_air.add(ye_olde_window_ac);
		
		ActionListener actionListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent actionEvent) {

				if (actionEvent.getSource() == light) {
					controlHub.setLightStatus(!controlHub.getLightStatus());
					
					if (controlHub.getLightStatus()) {
						light_socket.setIcon(light_on);
						light.setText("OFF");
					} else {
						light_socket.setIcon(light_off);
						light.setText("ON");
					}
				}
				
				if (actionEvent.getSource() == fan0 || actionEvent.getSource() == fan1 || actionEvent.getSource() == fan2) {
					if (fan0.isSelected()) {
						controlHub.setFanStatus(0);
						table_fan.setIcon(fan_off); //table_fan.setText("OFF");
					}
					if (fan1.isSelected()) {
						controlHub.setFanStatus(1);
						table_fan.setIcon(fan_low); //table_fan.setText("LOW");
					} 
					if (fan2.isSelected()) {
						controlHub.setFanStatus(2);
						table_fan.setIcon(fan_high); //table_fan.setText("HIGH");
					}
				}

			}
		};
		
		ChangeListener changeListener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				if (aircon.getValue() == 0) {
					controlHub.setACStatus(0);
					ye_olde_window_ac.setIcon(ac_off); //ye_olde_window_ac.setText("AC_OFF");
					ac_temp.setText("OFF");
				} else {
					controlHub.setACStatus(aircon.getValue());
					ye_olde_window_ac.setIcon(ac_on); //ye_olde_window_ac.setText("AC_ON");
					ac_temp.setText(controlHub.ac_status + " \u00B0"+ "C");
				}
				
			}
		};
			
		light.addActionListener(actionListener);
		fan0.addActionListener(actionListener);
		fan1.addActionListener(actionListener);
		fan2.addActionListener(actionListener);
		aircon.addChangeListener(changeListener);
		
		devices.add(d_light);
		devices.add(d_fan);
		devices.add(d_air);

		outerPanel.add(devices);
		
		add(headerPanel, BorderLayout.NORTH);
		add(outerPanel, BorderLayout.CENTER);
	
		setVisible(true);
		
		//Override Reset Trigger Date for testing...
		//resetTrigger = "Sep 13 2:16 a.m.";
		System.out.println(resetTrigger);
		System.out.println("-------");
		
		trackTime();

	}
	
	public void trackTime() {
		while(true) {
			
			date = dateFormat.format(Calendar.getInstance().getTime());
			time = timeFormat.format(Calendar.getInstance().getTime());
			trigger = resetUpdateFormat.format(Calendar.getInstance().getTime());
			
			clock.setText(date + "   " + time); //Different format for readability 
			
			//clock.setText(trigger);
			
			//if the month day hour match the declared value, toggle all devices off.
			//Swap If statements for testing.
			if (trigger.equalsIgnoreCase(resetTrigger) && resetFlag) {
			//if (trigger.equalsIgnoreCase(resetTrigger)) {
				controlHub.setLightStatus(false);
				controlHub.setFanStatus(0);
				controlHub.setACStatus(0);
				//Update UI to show new device status
				initControls();
				resetFlag = !resetFlag;
				System.out.println("reset " + date + " " + time);
			}
		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	 }
	
	/**/
	public void initControls() {
		//Read status from the controller for all devices and update the UI 
		if (controlHub.getLightStatus()) {
			light_socket.setIcon(light_on);
			light.setText("OFF");
		} else {
			light_socket.setIcon(light_off);
			light.setText("ON");
		}
		
		if(controlHub.getFanStatus() == 0 ) {
			table_fan.setIcon(fan_off); //table_fan.setText("FAN_OFF");
			fan0.setSelected(true);
		} else if (controlHub.getFanStatus() == 1 ) {
			table_fan.setIcon(fan_low); //table_fan.setText("FAN_LOW");
			fan1.setSelected(true);
		} else {
			table_fan.setIcon(fan_high); //table_fan.setText("FAN_HIGH");
			fan2.setSelected(true);
		}
		
		aircon.setValue(controlHub.getACStatus());
		if(controlHub.getACStatus() == 0 ) {
			ye_olde_window_ac.setIcon(ac_off); //ye_olde_window_ac.setText("AC_OFF");
			ac_temp.setText("OFF");
		} else {
			ye_olde_window_ac.setText("AC_ON");
			ye_olde_window_ac.setIcon(ac_on); //ac_temp.setText(controlHub.ac_status + " \u00B0"+ "C"); 
		}
	}
	
}
