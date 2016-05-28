package org.lcsim.detector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lcsim.detector.identifier.IIdentifier;

public class DetectorElementIdentifierHash
{
	private Map<Long, IDetectorElementContainer> idhash = new HashMap<Long, IDetectorElementContainer>(); 
	
	public void put(IDetectorElement de)
	{
		IIdentifier id = de.getIdentifier();
		
		// Ignore null id.
		if (id == null)
		{
			return;
		}
		
		// Ignore invalid id.
		if (!id.isValid())
		{
			return;
		}
		
		Long rawid = id.getValue();
		
		// Create list if doesn't exist.
		if (idhash.get(rawid) == null)
		{
			idhash.put(rawid, new DetectorElementContainer());
		}
		
		// Add a hash from id to DetectorElement.
		idhash.get(rawid).add(de);
	}
	
	public IDetectorElementContainer get(IIdentifier id)
	{
		return idhash.get(id.getValue());
	}
	
	public IDetectorElementContainer get(Long rawid)
	{
		return idhash.get(rawid);
	}
	
	public IDetectorElementContainer get(List<IIdentifier> ids)
	{
		IDetectorElementContainer ret = new DetectorElementContainer();
		for (IIdentifier id : ids)
		{
			IDetectorElementContainer src = get(id);
			ret.addAll(src);
		}
		return ret;
	}
	
	public void clear()
	{
		idhash.clear();
	}
}