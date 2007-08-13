;; Import the required ArgoUML packages
(import "org.argouml.ui.*")

;; Import the UML MM packages
(import "ru.novosoft.uml.foundation.core.*")
(import "ru.novosoft.uml.foundation.data_types.*")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Utility function to filter lists
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define (remove-if-not filter list)
  (if (null? list) 
      '()
      (if (filter (car list))
	  (cons (car list) (remove-if-not filter (cdr list)))
	  (remove-if-not filter (cdr list)))))


;; Utility functions to filter features from classifiers

;; Check if a feature is a attribute
(define (attribute? feature)
  (.isInstance MAttributeImpl.class feature))

;; Check if a feature is a operation
(define (operation? feature)
  (.isInstance MOperationImpl.class feature))

;; Add features to a classifier
(define (addFeature classifier feature)
  (.addFeature classifier feature))

;; Get all the features from a classifier
(define (getFeatures classifier)
  (array->list (.toArray (.getFeatures classifier))))

;; Get all the attributes from a classifier
(define (getAttributes classifier)
  (remove-if-not attribute? (getFeatures classifier)))

;; Get all the operations from a classifier
(define (getOperations classifier)
  (remove-if-not operation? (getFeatures classifier)))

;; Find a type in the current model
(define (getType type)
  (define projectBrowser ProjectBrowser.TheInstance$)
  (define project (.getProject projectBrowser))
  (.findType project type))

;; Create a new parameter
(define (newGenericParameter name type kind)
  (define param (MParameterImpl.))
  (.setName param name)
  (.setType param type)
  (.setKind param kind)
  param)

;; Create a new return type
(define (newReturnType type)
  (newGenericParameter "return" type MParameterDirectionKind.RETURN$))

(define (newParameter name type)
  (newGenericParameter name type MParameterDirectionKind.IN$))

;; Create a new Operation
(define (newOperation name returnType argument)
  (define operation (MOperationImpl.))
  (.setName operation name)
  (.addParameter operation returnType)
  (if (not (null? argument))
      (.addParameter operation argument))
  operation)

;;;;;;;;;;;;;;;;;;;;
;; The actual script
;;;;;;;;;;;;;;;;;;;;
(define (convertattr attrName)
  (define attr (if (eq? (string-ref attrName 0) #\_)
		   (substring attrName 1 (string-length attrName))
		   attrName))
  (string-append (string (char-upcase (string-ref attr 0))) (substring attr 1 (string-length attr))))
		   
(define (param->getOperation param)
  (newOperation (string-append "get" (convertattr (.getName param)))
		(newReturnType (.getType param))
		'()))

(define (param->setOperation param)
  (define attrName (.getName param))
  (newOperation (string-append "set" (convertattr attrName)) 
		(newReturnType (getType "void"))
		(newParameter (string-append "new_" attrName) (.getType param))))

;; The main method of the script
(define (main shellargs)  
  (define projectBrowser ProjectBrowser.TheInstance$)
  (define target (.getDetailsTarget projectBrowser))
  (define attributes (getAttributes target))
  (define win (javax.swing.JFrame. "Add get/set operations"))
  (define quit (javax.swing.JButton. "quit"))
  (define contents (.getContentPane win))

  (define (attribute-button attribute)
    (define button (javax.swing.JButton. (.getName attribute)))
    (.addActionListener button (Listener11. (lambda(e) (addFeature target (param->getOperation attribute)))))
    (.addActionListener button (Listener11. (lambda(e) (addFeature target (param->setOperation attribute)))))
    button)

  (.setLayout contents (java.awt.GridLayout. (+ (length attributes) 1) 1))
  (map (lambda (attribute) (.add contents (attribute-button attribute))) attributes)
  (.add contents  quit)
  (.addActionListener quit (Listener11. (lambda(e) (.hide win))))
  (.pack win)  
  (.show win)
)


