package br.com.bjbraz.repository

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository


@Repository
interface AccountRepository : ReactiveMongoRepository<AccountRepository, String> {


//    fun requestAccountTransfer(transferRequest: AccountTransferRequest) : Mono<Boolean>{
//        try{
//
//            if(transferRequest.from.isNullOrBlank()){
//                throw NullPointerException("CPF From Not Informed")
//            }
//
//            if(transferRequest.to.isNullOrBlank()){
//                throw NullPointerException("CPF To Not Informed")
//            }
//
//            if(transferRequest.amount == null){
//                throw NullPointerException("Invalid Amount")
//            }
//
//            if(transferRequest.amount.compareTo(0.toDouble()) == 0){
//                throw NullPointerException("Invalid Amount")
//            }
//
//            val from = cpfToFinancialOperationKey(transferRequest.from)
//            val to   = cpfToFinancialOperationKey(transferRequest.to)
//
//            if(from.isNullOrEmpty()){
//                throw NullPointerException("From FOK Not found with this CPF")
//            }
//
//            if(to.isNullOrEmpty()){
//                throw NullPointerException("TO FOK Not found with this CPF")
//            }
//
//            val param: Optional<ParameterEntity> = paramRepository.findByName("max_tx_amount")
//
//            if(!param.isPresent)
//                throw NullPointerException("Param max_tx_amount not found")
//
//            val maxTransferValue : Double = param.get().value!!.toDouble()
//
//            if(transferRequest.amount > maxTransferValue){
//                throw InvalidValueException("Invalid Amount")
//            }
//
//            val transfer: AccountTransferBody = AccountTransferBody(
//                    amount = transferRequest.amount,
//                    cpf = transferRequest.to,
//                    idempotencyKey = UUID.randomUUID().toString(),
//                    summary = "Account Transfer Request",
//                    transferCode = "Account Charge",
//                    toFinancialOperationKey = to,
//                    fromFinancialOperationKey = from
//            )
//
//            sqsMessage.sendMessageAccountTransferRequest(transfer)
//            return Mono.justOrEmpty(true)
//        }catch (ex:Exception){
//            logger.error("Error on put transfer $ex")
//        }
//
//        return Mono.justOrEmpty(false)
//    }
//
//    fun findByCpf(cpf: String): Item? = find(cpf)
//
//    fun find(cpf: String): Item?{
//        val query = QuerySpec()
//                .withKeyConditionExpression("cpf = :v_pk_type")
//                .withValueMap(ValueMap()
//                        .withString(":v_pk_type", cpf)
//                )
//        val items = cpfIndex.query(query)
//        return if (items != null && items.iterator().hasNext()) items.iterator().next() else ItemUtils.toItem(mapOf<String, AttributeValue>(
//                "cpf" to AttributeValue("")
//        ))
//    }
//
//    fun findAll(): MutableList<MutableMap<String, AttributeValue>>? {
//
//        val req = ScanRequest(tableName)
//
//        var result = dynamoDBAsync.scan(req)
//
//        var items = result.getItems();
//
//        return  items
//    }
//
//    fun save(account: Account?) : Mono<String>{
//        logger.info("Saving account $account")
//
//        var fok = "N/A"
//
//        account?.financialOperationKey?.let { fok = it }
//
//        var item = Item()
//                .withPrimaryKey("cpf", account!!.responsible!!.identifierDocument!!.document)
//
//        item.with("accountNumber", Random().nextInt())
//        item.with("documentNumber", account!!.responsible!!.documents!![0].document)
//        item.with("dt_ref", Date().time)
//        item.with("email", account!!.responsible!!.email)
//        item.with("firebaseId", "123")
//        item.with("mobileNumber", account!!.responsible!!.mobilePhone)
//        item.with("name", account!!.responsible!!.name)
//        item.with("cardName", account!!.responsible!!.cardName)
//        item.with("phoneNumber", account!!.responsible!!.businessPhone)
//        item.with("idempotencyKey", account!!.idempotencyKey)
//        item.with("financialOperationKey", fok)
//        item.with("status", account!!.status )
//
//        val dynamo = DynamoDB(dynamoDBAsync)
//        val table = dynamo.getTable(tableName)
//        table.putItem(item)
//
//        return Mono.justOrEmpty(account!!.owner!!.identifierDocument!!.document)
//    }
//
//    fun saveItem(item: Item) : Mono<String>{
//        val dynamo = DynamoDB(dynamoDBAsync)
//        val table = dynamo.getTable(tableName)
//
//        table.putItem(item)
//        return Mono.justOrEmpty(item.getString("cpf"))
//    }
//
//    fun requestDefaultCard(cpf:String): String?{
//        val item:Item = find(cpf)!!
//        if(item.get("name") != null){
//            sendMessageRequestCard(cpf= cpf, fok = item.getString("financialOperationKey"))
//            return cpf
//        }
//        return null
//    }
//
//    fun cpfToFinancialOperationKey(cpf:String): String?{
//        val query = QuerySpec()
//                .withKeyConditionExpression("cpf = :v_pk_type")
//                .withValueMap(ValueMap()
//                        .withString(":v_pk_type", cpf)
//                )
//        val items = cpfIndex.query(query)
//        return if (items != null && items.iterator().hasNext()) items.iterator().next().getString("financialOperationKey") else null
//    }
//
//    fun getItemByCPF(cpf:String): Item?{
//        val query = QuerySpec()
//                .withKeyConditionExpression("cpf = :v_pk_type")
//                .withValueMap(ValueMap()
//                        .withString(":v_pk_type", cpf)
//                )
//        val items = cpfIndex.query(query)
//        return if (items != null && items.iterator().hasNext()) items.iterator().next() else null
//    }
//
//    private fun sendMessageRequestCard(cpf: String, fok:String){
//        sqsMessage.sendMessageCardCreation(cpf = cpf, fok = fok, orderNumber = UUID.randomUUID().toString())
//    }
//
//    fun saveAndSend(account: Account?) : Mono<String>{
//        logger.info("Saving account $account")
//
//        if(null == account!!.idempotencyKey){
//            account!!.idempotencyKey = UUID.randomUUID().toString()
//        }
//
//        logger.info("Saving account $account with idempotencykey ${account.idempotencyKey}")
//
//        var item = Item()
//                .withPrimaryKey("cpf", account!!.responsible!!.identifierDocument!!.document)
//
//        item.with("accountNumber", Random().nextInt())
//        item.with("documentNumber", account!!.responsible!!.documents!![0].document)
//        item.with("dt_ref", Date().time)
//        item.with("email", account!!.responsible!!.email)
//        item.with("firebaseId", "123")
//        item.with("mobileNumber", account!!.responsible!!.mobilePhone)
//        item.with("name", account!!.responsible!!.name)
//        item.with("cardName", account!!.responsible!!.cardName)
//        item.with("birthDate", account!!.responsible!!.birthDate)
//        item.with("phoneNumber", account!!.responsible!!.businessPhone)
//        item.with("idempotencyKey", UUID.randomUUID().toString())
//        item.with("financialOperationKey", "N/A")
//
//        val dynamo = DynamoDB(dynamoDBAsync)
//        val table = dynamo.getTable(tableName)
//        table.putItem(item)
//
//        sqsMessage.sendMessageAccountCreation(account)
//
//        return Mono.justOrEmpty(account!!.responsible!!.identifierDocument!!.document)
//    }
//
//    fun putCommandOnQueue(identifier: String, command: Command): String? {
//        try{
//            sqsMessage.sendCommandMessage(CommandMessage(command = command.name, type = command.type, identifier = identifier))
//            return "OK"
//        }catch (ex:Exception){
//            logger.error("Error on putCommandOnQueue $ex")
//        }
//        return null
//    }
//
//    fun putCommandOnTestQueue(message: String): String? {
//        try{
//            sqsMessage.sendTestMessage(message)
//            return "OK $message"
//        }catch (ex:Exception){
//            logger.error("Error on putCommandOnQueue $ex")
//        }
//        return null
//    }

}