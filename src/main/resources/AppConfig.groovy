import com.objectdynamics.naturaltreatments.services.ConditionService
import com.objectdynamics.naturaltreatments.services.SubstanceService
import com.objectdynamics.naturaltreatments.services.TreatmentService
import com.peopletechnologies.onlinestore.db.DBAccess
import com.peopletechnologies.onlinestore.rest.CategoryResource
import com.peopletechnologies.onlinestore.rest.MediaResource
import com.peopletechnologies.onlinestore.services.DiscountProcessor
import com.peopletechnologies.onlinestore.services.MediaService
import com.peopletechnologies.onlinestore.services.discount.BuyXofAgetYofAFree
import com.peopletechnologies.onlinestore.rest.CartResource
import com.peopletechnologies.onlinestore.rest.PaymentResource
import com.peopletechnologies.onlinestore.rest.ProductResource
import com.peopletechnologies.onlinestore.rest.RestService
import com.peopletechnologies.onlinestore.services.CategoryService
import com.peopletechnologies.onlinestore.services.DataLoader
import com.peopletechnologies.onlinestore.services.PaymentProcessor
import com.peopletechnologies.onlinestore.services.ProductService
import com.peopletechnologies.onlinestore.services.UserService
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.gridfs.GridFsTemplate

/// Groovy Spring Context
Properties properties = new Properties();
File propFile = new File("/opt/natural-treatments/natural-treatments.properties")
properties.load(new FileInputStream(propFile))

beans {
  xmlns([ctx: 'http://www.springframework.org/schema/context'])
  xmlns([mongo: "http://www.springframework.org/schema/data/mongo"])

  ctx.'component-scan'('base-package': "com.objectdynamics")

  ////////////////////////////////////////////////////////////////
  ///  Connector Objects for DB
  ////////////////////////////////////////////////////////////////

  mongo.mongo( 'id':"mongo", 'replica-set':"${properties['mongo.host']}")
  mongo.'db-factory'( 'id':"mongoDbFactory", 'dbname':"${properties['db.name']}", 'mongo-ref':'mongo')
  mongo.'mapping-converter'( "id":"mongoConverter", "db-factory-ref" : "mongoDbFactory" )

  mongoTemplate(MongoTemplate) { beanDefinition ->
    beanDefinition.constructorArgs = [ref("mongoDbFactory"), ref("mongoConverter")]
    writeConcern = "SAFE"
  }

  gridFsTemplate(GridFsTemplate){ beanDefinition ->
    beanDefinition.constructorArgs = [
      ref(mongoDbFactory),
      ref("mongoConverter")
    ]
  }

  dbAccess(DBAccess){ beanDefinition ->
    beanDefinition.constructorArgs = [ref("mongoTemplate")]
  }

  /////////////////////////////////////////////////////////////////
  //// Services
  /////////////////////////////////////////////////////////////////

  conditionService(ConditionService){ beanDefinition ->
    beanDefinition.constructorArgs =[ref('dbAccess')]
  }

  substanceService(SubstanceService) { beanDefinition ->
    beanDefinition.constructorArgs =[ref('dbAccess')]
  }

  treatmentService(TreatmentService) { beanDefinition ->
    beanDefinition.constructorArgs =[ref('dbAccess')]
  }

  /////////////////////////////////////////////////////////////////
  //// REST Resources
  /////////////////////////////////////////////////////////////////
  substanceResource(CartResource){ beanDefinition ->
    beanDefinition.constructorArgs = [ref('userService')]
  }

  mediaResource(MediaResource){ beanDefinition ->
    beanDefinition.constructorArgs = [ref('mediaService')]
  }

  categoryResource(CategoryResource){ beanDefinition ->
    beanDefinition.constructorArgs = [ref('categoryService')]
  }

  paymentResource(PaymentResource){ beanDefinition ->
    beanDefinition.constructorArgs = [ref('paymentProcessor')]
  }

  productResource(ProductResource){ beanDefinition ->
    beanDefinition.constructorArgs = [ref('productService')]
  }

  /////////////////////////////////////////////////////////////////
  //// REST Server Instance
  /////////////////////////////////////////////////////////////////
  restService(RestService) { beanDefinition ->
    beanDefinition.constructorArgs = [
      ref('productResource'),
      ref('cartResource'),
      ref('paymentResource'),
      ref('categoryResource'),
      ref('mediaResource'),
    ]
  }

  /////////////////////////////////////////////////////////////////
  //// Data Loader
  /////////////////////////////////////////////////////////////////
  dataLoader(DataLoader){
    beanDefinition ->
      beanDefinition.constructorArgs = [
        ref('productService'),
        ref('categoryService'),
        ref('userService'),
        ref('mediaService')]
  }

}
