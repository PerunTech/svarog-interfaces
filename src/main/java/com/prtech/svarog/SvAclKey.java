package com.prtech.svarog;

import com.prtech.svarog_common.DbDataObject;

/**
 * Enumeration holding flags about access over svarog objects.
 * 
 * @author XPS13
 *
 */
public class SvAclKey {
	private final long objectId;
	private final long objectTypeId;
	private final int hash;

	SvAclKey(DbDataObject dboAcl) {
		if (dboAcl.getVal("acl_object_id") != null)
			this.objectId = (Long) dboAcl.getVal("acl_object_id");
		else
			this.objectId = dboAcl.getObject_id();
		if (dboAcl.getVal("acl_object_type") != null)
			this.objectTypeId = (Long) dboAcl.getVal("acl_object_type");
		else
			this.objectTypeId = dboAcl.getObject_type();

		hash = (Long.toString(objectId) + "-" + Long.toString(objectTypeId)).hashCode();
	}

	public long getObjectId() {
		return objectId;
	}

	public long getObjectTypeId() {
		return objectTypeId;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	/**
	 * Overriden equals operator
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SvAclKey)) {
			return false;
		}
		return (Long.toString(objectId) + "-" + Long.toString(objectTypeId)).equals(
				Long.toString(((SvAclKey) obj).objectId) + "-" + Long.toString(((SvAclKey) obj).objectTypeId));
	}

}
