package jinji.game;

public class Shape {
	
	private int shapeType;
	//0 = pointUp, 1 = pointRight, 2 = pointDown, 3 = pointLeft
	private int rotation;
	
	private final int I = 0;
	private final int O = 1;
	private final int T = 2;
	private final int S = 3;
	private final int Z = 4;
	private final int J = 5;
	private final int L = 6;
	
	//I-rotation shapes as Boolean arrays
	private final Boolean I_U[] = 
		{false, false, false, false,
		true, true, true, true,
		false, false, false, false,
		false, false, false, false};
	private final Boolean I_R[] = 
		{false, false, true, false,
		false, false, true, false,
		false, false, true, false,
		false, false, true, false};
	private final Boolean I_D[] = 
		{false, false, false, false,
		false, false, false, false,
		true, true, true, true,
		false, false, false, false};
	private final Boolean I_L[] = 
		{false, true, false, false,
		false, true, false, false,
		false, true, false, false,
		false, true, false, false};
	
	//O-rotation shapes as Boolean arrays
	private final Boolean O_U[] = 
		{false, false, false, false,
		false, true, true, false,
		false, true, true, false,
		false, false, false, false};
	
	//T-rotation shapes as Boolean arrays
	private final Boolean T_U[] = 
		{false, true, false, false,
		true, true, true, false,
		false, false, false, false,
		false, false, false, false};
	private final Boolean T_R[] = 
		{false, true, false, false,
		false, true, true, false,
		false, true, false, false,
		false, false, false, false};
	private final Boolean T_D[] = 
		{false, false, false, false,
		true, true, true, false,
		false, true, false, false,
		false, false, false, false};
	private final Boolean T_L[] = 
		{false, true, false, false,
		true, true, false, false,
		false, true, false, false,
		false, false, false, false};
	
	//S-rotation shapes as Boolean arrays
	private final Boolean S_U[] = 
		{false, true, true, false,
		true, true, false, false,
		false, false, false, false,
		false, false, false, false};
	private final Boolean S_R[] = 
		{false, true, false, false,
		false, true, true, false,
		false, false, true, false,
		false, false, false, false};
	private final Boolean S_D[] = 
		{false, false, false, false,
		false, true, true, false,
		true, true, false, false,
		false, false, false, false};
	private final Boolean S_L[] = 
		{true, false, false, false,
		true, true, false, false,
		false, true, false, false,
		false, false, false, false};
	
	//Z-rotation shapes as Boolean arrays
	private final Boolean Z_U[] = 
		{true, true, false, false,
		false, true, true, false,
		false, false, false, false,
		false, false, false, false};
	private final Boolean Z_R[] = 
		{false, false, true, false,
		false, true, true, false,
		false, true, false, false,
		false, false, false, false};
	private final Boolean Z_D[] = 
		{false, false, false, false,
		true, true, false, false,
		false, true, true, false,
		false, false, false, false};
	private final Boolean Z_L[] = 
		{false, true, false, false,
		true, true, false, false,
		true, false, false, false,
		false, false, false, false};
	
	//J-rotation shapes as Boolean arrays
	private final Boolean J_U[] = 
		{true, false, false, false,
		true, true, true, false,
		false, false, false, false,
		false, false, false, false};
	private final Boolean J_R[] = 
		{false, true, true, false,
		false, true, false, false,
		false, true, false, false,
		false, false, false, false};
	private final Boolean J_D[] = 
		{false, false, false, false,
		true, true, true, false,
		false, false, true, false,
		false, false, false, false};
	private final Boolean J_L[] = 
		{false, true, false, false,
		false, true, false, false,
		true, true, false, false,
		false, false, false, false};
	
	//J-rotation shapes as Boolean arrays
	private final Boolean L_U[] = 
		{false, false, true, false,
		true, true, true, false,
		false, false, false, false,
		false, false, false, false};
	private final Boolean L_R[] = 
		{false, true, false, false,
		false, true, false, false,
		false, true, true, false,
		false, false, false, false};
	private final Boolean L_D[] = 
		{false, false, false, false,
		true, true, true, false,
		true, false, false, false,
		false, false, false, false};
	private final Boolean L_L[] = 
		{true, true, false, false,
		false, true, false, false,
		false, true, false, false,
		false, false, false, false};
	
	
	public Shape(int type) {
		shapeType = type;
	}
	
	public void setShape(int shape) {
		shapeType = shape;
	}
	
	public void setRotation(int rot) {
		rotation = rot;
		if(rot == 4) {
			rotation = 0;
		}
		if(rot == -1) {
			rotation = 3;
		}
	}
	
	public int getNextRotCW() {
		if(rotation == 3) {
			return 0;
		}
		return rotation+1;
	}

	public int getNextRotCCW() {
		if(rotation == 0) {
			return 3;
		}
		return rotation-1;
	}
	
	public int getShape() {
		return shapeType;
	}
	
	public int getRotation() {
		return rotation;
	}
	
	public Boolean[] getShapeMap() {
		switch(shapeType) {
		case I:
			if(rotation == 0) {
				return I_U;
			}else if(rotation == 1) {
				return I_R;
			}else if(rotation == 2) {
				return I_D;
			}else if(rotation == 3) {
				return I_L;
			}
			break;
		case O:
			return O_U;
		case T:
			if(rotation == 0) {
				return T_U;
			}else if(rotation == 1) {
				return T_R;
			}else if(rotation == 2) {
				return T_D;
			}else if(rotation == 3) {
				return T_L;
			}
			break;
		case S:
			if(rotation == 0) {
				return S_U;
			}else if(rotation == 1) {
				return S_R;
			}else if(rotation == 2) {
				return S_D;
			}else if(rotation == 3) {
				return S_L;
			}
			break;
		case Z:
			if(rotation == 0) {
				return Z_U;
			}else if(rotation == 1) {
				return Z_R;
			}else if(rotation == 2) {
				return Z_D;
			}else if(rotation == 3) {
				return Z_L;
			}
			break;
		case J:
			if(rotation == 0) {
				return J_U;
			}else if(rotation == 1) {
				return J_R;
			}else if(rotation == 2) {
				return J_D;
			}else if(rotation == 3) {
				return J_L;
			}
			break;
		case L:
			if(rotation == 0) {
				return L_U;
			}else if(rotation == 1) {
				return L_R;
			}else if(rotation == 2) {
				return L_D;
			}else if(rotation == 3) {
				return L_L;
			}
			break;
		}
		
		return new Boolean[] {false};
	}
	
	//Generic shapemap getter from shape and rotation
	public Boolean[] getShapeMap(int shape, int rot) {
		switch(shape) {
		case I:
			if(rot == 0) {
				return I_U;
			}else if(rot == 1) {
				return I_R;
			}else if(rot == 2) {
				return I_D;
			}else if(rot == 3) {
				return I_L;
			}
			break;
		case O:
			return O_U;
		case T:
			if(rot == 0) {
				return T_U;
			}else if(rot == 1) {
				return T_R;
			}else if(rot == 2) {
				return T_D;
			}else if(rot == 3) {
				return T_L;
			}
			break;
		case S:
			if(rot == 0) {
				return S_U;
			}else if(rot == 1) {
				return S_R;
			}else if(rot == 2) {
				return S_D;
			}else if(rot == 3) {
				return S_L;
			}
			break;
		case Z:
			if(rot == 0) {
				return Z_U;
			}else if(rot == 1) {
				return Z_R;
			}else if(rot == 2) {
				return Z_D;
			}else if(rot == 3) {
				return Z_L;
			}
			break;
		case J:
			if(rot == 0) {
				return J_U;
			}else if(rot == 1) {
				return J_R;
			}else if(rot == 2) {
				return J_D;
			}else if(rot == 3) {
				return J_L;
			}
			break;
		case L:
			if(rot == 0) {
				return L_U;
			}else if(rot == 1) {
				return L_R;
			}else if(rot == 2) {
				return L_D;
			}else if(rot == 3) {
				return L_L;
			}
			break;
		}
		
		return new Boolean[] {false};
	}
	
}
