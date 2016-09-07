package com.objectdynamics.naturaltreatments.model

/**
 * Represents a treatment for a particular Condition
 *
 */
class Treatment {
  Condition condition;
  List<Substance> substances;
  String dosage;
  String expectedResults;
  List<String> links;
  List<String> testimonialLinks;// if not a link then the text is used
  String description;
  String specificBrands;
}
