package moxi.core.demo.service.contract.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import moxi.core.demo.constans.CommonConstants;
import moxi.core.demo.dao.contract.TContractPlanChangeMapper;
import moxi.core.demo.exceptions.AppRuntimeException;
import moxi.core.demo.model.contract.ContractPlanChangeDO;
import moxi.core.demo.model.contract.TContractPlanChange;
import moxi.core.demo.model.wallet.WalletLogVO;
import moxi.core.demo.service.contract.ITContractPlanChangeService;
import moxi.core.demo.service.wallet.ICustomerWalletLogService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 合同执行计划变动 服务实现类   ---- 终止任务
 * </p>
 *
 * @author winter
 * @since 2019-02-16
 */
@Service
public class ContractModifyTaskPriceServiceImpl extends ServiceImpl<TContractPlanChangeMapper, TContractPlanChange> implements ITContractPlanChangeService {

    @Resource
    private ICustomerWalletLogService customerWalletLogService;

    public void contractPlanChange(@Validated ContractPlanChangeDO contractPlanChangeDO){


        EntityWrapper<TContractPlanChange> condition = new EntityWrapper<>();
        condition.eq("customer_id", contractPlanChangeDO.getCustomerId());
        condition.eq("product_id", contractPlanChangeDO.getProductId());
        condition.eq("contract_id", contractPlanChangeDO.getContractId());
        condition.eq("contract_product_id", contractPlanChangeDO.getContractProductId());

        condition.orderBy("create_time", false);
        TContractPlanChange contractPlanChange1 = this.selectOne(condition);

        if (contractPlanChange1 == null) return;

        TContractPlanChange contractPlanChange = new TContractPlanChange();
        //客户id
        contractPlanChange.setCustomerId(contractPlanChange1.getCustomerId());
        //子合同id
        contractPlanChange.setContractProductId(contractPlanChange1.getContractProductId());
        //产品id
        contractPlanChange.setProductId(contractPlanChange1.getProductId());
        //合同id
        contractPlanChange.setContractId(contractPlanChange1.getContractId());
        contractPlanChange.setTaskId(contractPlanChange1.getTaskId());


        //单价
        contractPlanChange.setPrice(contractPlanChangeDO.getPrice());
        contractPlanChange.setPriceChange(contractPlanChangeDO.getPrice().subtract(contractPlanChange1.getPrice()));

        //剩余赠送周期
        contractPlanChange.setRemainingGiftPeriod(contractPlanChange1.getRemainingGiftPeriod());

        //剩余服务周期
        contractPlanChange.setRemainingPeriod(contractPlanChange1.getRemainingPeriod());

        //支出预算
        contractPlanChange.setExpenditureBudget(contractPlanChange1.getExpenditureBudget());

        //计划收款
        BigDecimal plannedAmount  = this.plannedReceivable(contractPlanChange, contractPlanChangeDO);

        contractPlanChange.setPlannedCollection(plannedAmount);
        contractPlanChange.setPlannedCollectionChange(plannedAmount.subtract(contractPlanChange1.getPlannedCollection()));
        //操作类型
        contractPlanChange.setType(CommonConstants.CONTRACT_PLAN_MODIFY_TASK_PRICE);
        contractPlanChange.setCreateTime(contractPlanChangeDO.getCreateTime());
        this.insert(contractPlanChange);
    }

    /**
     * 计算计划应收
     * */
    public BigDecimal plannedReceivable(TContractPlanChange contractPlanChange,ContractPlanChangeDO contractPlanChangeDO){
        //计算
        WalletLogVO walletLogVO = new WalletLogVO();
        walletLogVO.setContractProductId(contractPlanChange.getContractProductId());
        BigDecimal taskAmount = customerWalletLogService.taskTotalAmount(walletLogVO);
        taskAmount = taskAmount == null ? new BigDecimal("0") : taskAmount;
        BigDecimal amount;
        if (contractPlanChangeDO == null){
             amount = contractPlanChange.getPrice().multiply(new BigDecimal(contractPlanChange.getRemainingPeriod().toString()));
        }else{
             amount = contractPlanChangeDO.getPrice().multiply(new BigDecimal(contractPlanChange.getRemainingPeriod().toString()));
        }
        BigDecimal penaltyAmount = customerWalletLogService.penaltyTotalAmount(walletLogVO);
        return taskAmount.add(amount).add(penaltyAmount);
    }

}
