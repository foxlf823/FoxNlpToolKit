package cn.fox.math;

import java.io.Serializable;

/*
 * if it's used as a vector, keep r=1
 */
public class Matrix implements Serializable{

	private static final long serialVersionUID = -8784883831575027665L;
	private double [][] data;
	private int r;  // row
	private int c;  // column
	
	public Matrix(int _r, int _c, double... _data) {
		r = _r;
		c = _c;
		data = new double[_r][_c];
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				data[i][j] = _data[i*c+j];
			}
		}
	}
	
	public Matrix(int _r, int _c) {
		r = _r;
		c = _c;
		data = new double[_r][_c];
	}
	
	// Copy a new Matrix based on this object.
	public Matrix copy() {
		Matrix ret = new Matrix(r, c);
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				ret.data[i][j] = data[i][j];
			}
		}
		return ret;
	}
	
	public double getElement(int i, int j)  {
		
		return data[i][j];
	}
	
	public void setElement(int i, int j, double value)  {
		
		data[i][j] = value;
	}
	
	public int getRowNumber() {
		return r;
	}
	
	public int getColNumber() {
		return c;
	}
	
	// this = u
	public void assign(Matrix u)  {
		
		
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				data[i][j] = u.getElement(i, j);
			}
		}
		
		
	}
	
	@Override
	public String toString() {
		
		String s = "(\n";
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				s += data[i][j]+" ";
				
			}
			
			s += "\n";
			
		}
		s += ")";

		return s;
	}
	
	// u+v
	public static Matrix add(Matrix u, Matrix v)  {
		
		
		Matrix ret = new Matrix(u.getRowNumber(), u.getColNumber());
		for(int i=0;i<u.getRowNumber();i++) {
			for(int j=0;j<u.getColNumber();j++) {
				ret.setElement(i, j, u.getElement(i, j)+v.getElement(i, j));
			}
			
		}
		
		return ret;
	}
	
	// this = this+v
	public void addThis(Matrix v) {
		
		
		for(int i=0;i<this.getRowNumber();i++) {
			for(int j=0;j<this.getColNumber();j++) {
				this.setElement(i, j, this.getElement(i, j)+v.getElement(i, j));
			}
			
		}
		
		
	}
	// u+v
	public static Matrix mul(Matrix u, Matrix v)  {
		
		
		Matrix ret = new Matrix(u.getRowNumber(), v.getColNumber());
		for(int i=0;i<u.getRowNumber();i++) {
			for(int j=0;j<v.getColNumber();j++) {
				double value=0;
				for(int k=0;k<u.getColNumber();k++) {
					value +=u.getElement(i, k)*v.getElement(k, j);
				}
				ret.setElement(i, j, value);
			}
		}
		
		return ret;
	}
	// num*v
	public static Matrix numMul(double num, Matrix v)  {
		
		Matrix ret = new Matrix(v.getRowNumber(), v.getColNumber());
		ret.assign(v);
		for(int i=0;i<ret.getRowNumber();i++) {
			for(int j=0;j<ret.getColNumber();j++) {
				ret.setElement(i, j, ret.getElement(i, j)*num);
			}
		}
		
		return ret;
	}
	// this = num*this
	public void numMulThis(double num)  {

		for(int i=0;i<this.getRowNumber();i++) {
			for(int j=0;j<this.getColNumber();j++) {
				this.setElement(i, j, this.getElement(i, j)*num);
			}
		}
		
	}
	// (u)T
	public static Matrix transpose(Matrix u) {
		Matrix ret = new Matrix(u.getColNumber(), u.getRowNumber());		
		for(int i=0;i<ret.getRowNumber();i++) {
			for(int j=0;j<ret.getColNumber();j++) {
				ret.setElement(i, j, u.getElement(j, i));
			}
		}
		
		return ret;
	}
	// Convert this object to a value and this object must be 1 row and 1 col.
	public double toScalar()  {
		
		
		return this.getElement(0, 0);
	}
	// This function must be used by vectors.
	public static double distanceEuclidean(Matrix u, Matrix v) {
		
		
		double sum = 0;  
	    for(int i=0; i<u.getColNumber(); ++i)  
	    {  
	        sum += (u.getElement(0, i)-v.getElement(0, i)) * (u.getElement(0, i)-v.getElement(0, i));  
	    }  
	    return Math.sqrt(sum);  
	}
	// Fill this object with "number"
	public void fill(double number) {
		for(int i=0;i<this.getRowNumber();i++) {
			for(int j=0;j<this.getColNumber();j++) {
				this.setElement(i, j, number);
			}
		}
	}
	// This function must be used by vectors. value = u*v
	public static double innerProduct(Matrix u, Matrix v) {
		
		double sum = 0;
		for(int i=0;i<u.getColNumber();i++) {
			sum += u.getElement(0, i)*v.getElement(0, i);
		}
		return sum;
	}
	// Used by vectors and get the norm(also known as length) of this object.
	public double norm() {
		double sum = 0;
		for(int i=0;i<this.getColNumber();i++) {
			sum += this.getElement(0, i)*this.getElement(0, i);
		}
		sum = Math.sqrt(sum);
		return sum;
	}
	
	// Used by vectors
	// append b vector to the end of at
	public static Matrix append(Matrix a, Matrix b) {
		Matrix ret = new Matrix(1, a.getColNumber()+b.getColNumber());
		for(int i=0;i<a.getColNumber();i++) {
			ret.setElement(0, i, a.getElement(0, i));
		}
		for(int i=0;i<b.getColNumber();i++) {
			ret.setElement(0, i+a.getColNumber(), b.getElement(0, i));
		}
		return ret;
	}
	
	// Used by vectors
	// Get sub-vector( begin to end-1 ) of this object
	public Matrix subVector(int begin, int end) {
		Matrix ret = new Matrix(1, end-begin);
		for(int i=0;i<ret.getColNumber();i++) {
			ret.setElement(0, i, this.getElement(0, i+begin));
		}
		return ret;
	}
	
	public static double cosSimilarity(Matrix x, Matrix y) {
		try {
			double numerator = Matrix.innerProduct(x, y);
			double denominator = x.norm()*y.norm();
			return numerator/denominator;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof Matrix))
			return false;
		Matrix o = (Matrix)obj;
		
		for(int i=0;i<r;i++) {
			for(int j=0;j<c;j++) {
				if(data[i][j] != o.data[i][j])
					return false;
			}
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		
		int seed = 131; // 31 131 1313 13131 131313 etc..  
	    int hash = 0;  
	    for(int i = 0; i < r; i++)  
	    {  
	    	for(int j=0;j<c;j++) {
	    		hash = (hash * seed) + i+j+(int)(data[i][j]*13131);  
			}
	    }  
	    return hash;  
	}
}
