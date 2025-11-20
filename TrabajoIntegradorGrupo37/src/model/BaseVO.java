package model;

public abstract class BaseVO {
protected int id;
protected boolean eliminado;

protected BaseVO(int id, boolean eliminado) {
    this.id = id;
    this.eliminado = eliminado;
}

protected BaseVO() {
	this.eliminado=false;
}
public boolean isEliminado() {
	return eliminado;
}
public void setEliminado(boolean eliminado) {
	this.eliminado = eliminado;
}
public int getId() {
	return id;
}

public void setId(int id) {
	this.id=id;
}

}
