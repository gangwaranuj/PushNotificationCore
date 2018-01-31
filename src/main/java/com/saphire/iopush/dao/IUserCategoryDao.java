package com.saphire.iopush.dao;

import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.utils.Response;

public interface IUserCategoryDao {
	
	Response updateSubscriberBalanceAndUsed(int product_id);
	
	Response userCategoryDetails(int product_id);
	
	Response updateUserCategoryDetails(IopushUsercategory UserCategory);
	
	Response insertUserCategoryDetails(IopushUsercategory UserCategory);
}
