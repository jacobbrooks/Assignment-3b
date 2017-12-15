import java.awt.Color;
import java.util.ArrayList;

public class Screen {
	
	private int width, height;
	public int[] pixels;
	private Thread t;
	Control c;
	
	public Screen(int width, int height, Control c) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		this.c = c;
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}

	public int getColor(double temperature){
		if(temperature == 0.0) {
			return 0xC3C3C3;
		}else if (temperature < 100.0 / 200000000.0){
			return 0xFF0000;
		}else if (temperature < 100.0 / 2000000.0){
			return 0xFFB200; 
		}else if(temperature <= 100.0){
			return 0xFFFF00;
		}else {
			return 0xFFFFFF;
		}
	}

	public void render() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[x + (y * width)] = getColor(c.alloy[y][x]);
			}
		}
	}
}
