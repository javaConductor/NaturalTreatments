package com.objectdynamics.naturaltreatments.services

import com.objectdynamics.naturaltreatments.model.Condition
import com.objectdynamics.naturaltreatments.model.Substance
import com.objectdynamics.naturaltreatments.model.Treatment
import com.objectdynamics.naturaltreatments.persistence.DBAccess

/**
 * Created by lcollins on 9/7/2016.
 */
class TreatmentService {
DBAccess dbAccess ;

  Treatment createTreatment( Condition c, List<Substance> substances ){
    if (!c || substances == null || substances.size() == 0){
      return null;
    }
    Treatment newOne = new Treatment(condition: c, substances: substances);
    return dbAccess.saveTreatment(newOne);
  };

  Treatment findTreatmentForCondition( Condition c ){

  };

}
