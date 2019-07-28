package be.nvcursus.JUnit;

public class Temperature {
	private float value;

	public Temperature(float t) throws InvalidTemperatureException {
		setValue(t);
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) throws InvalidTemperatureException {
		if (value >= -273.15F) {
			this.value = value;
		} else {
			throw new InvalidTemperatureException("temperature cannot be lower than -273.15");
		}
	}
	
	public boolean isBoiling() {
		return value >= 100.0F;
	}
	
	public boolean isFreezing() {
		return value <= 0.0F;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(value);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Temperature other = (Temperature) obj;
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
			return false;
		return true;
	}
}
