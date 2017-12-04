package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillException;
import org.seckill.exception.SeckillSloseException;
import org.seckill.service.SecckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;


/**
 * Created by Jodie on 2017/7/30.
 */
@Controller
@RequestMapping("/seckill")
public class SecckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecckillService secckillService;

    /**
     * 列表页
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String list(Model model) {
        List<Seckill> seckillList = secckillService.getSeckillList();
        model.addAttribute("seckillList", seckillList);
        return "list";
    }

    /**
     * 详情页
     *
     * @return
     */
    @RequestMapping(value = "{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";//重定向到这个页面
        }
        Seckill seckill = secckillService.queryById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 秒杀开启时输出秒杀接口的地址，否则输出系统使用时间和秒杀时间
     *
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody //告诉返回的是json类型
    public SeckillResult<Exposer> /*TODO*/ exposer(Long seckillId) {

        SeckillResult<Exposer> result;
        try {
            Exposer exposer = secckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<>(true, exposer);//初始化result
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone", required = false) Long phone
    ) {
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution seckillExecution = secckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);
        } catch (RepeatKillException e) {
            SeckillExecution exception = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false, exception);
        } catch (SeckillSloseException e) {
            SeckillExecution exception = new SeckillExecution(seckillId, SeckillStatEnum.EDN);
            return new SeckillResult<SeckillExecution>(false, exception);
        } catch (Exception e) {
            SeckillExecution exception = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false, exception);
        }
    }

    @RequestMapping(value = "time/now", method = RequestMethod.GET)
    public SeckillResult<Long> time() {
        Date data = new Date();
        return new SeckillResult(true, data.getTime());
    }
}
