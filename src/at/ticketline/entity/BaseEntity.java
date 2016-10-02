package at.ticketline.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {

	@Version
	protected int version = 0;

	public int getVersion() {
		return this.version;
	}
}
