package com.app.leon.abfa.Models.DbTables;

import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.app.leon.abfa.Models.InterCommunation.OffLoad;
import com.app.leon.abfa.Models.InterCommunation.OffLoadParams;
import com.app.leon.abfa.Models.InterCommunation.OnLoad;
import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class OnOffLoad extends SugarRecord {
    public String idCusotm;
    public Integer pageNumber;
    public int offLoadStateId;

    //region OnLoad members
    public int zoneId;
    public String listNumber;
    public int trackNumber;
    public String billId;
    public int radif;
    public String eshterak;
    public String qeraatCode;
    public String name;
    public String family;
    public String address;
    public Integer karbariCode;
    public Integer tedadMaskooni;
    public Integer tedadNonMaskooni;
    public Integer tedadKol;
    public Integer qotr;
    public Integer sifoonQotr;
    public Integer preNumber;
    public Float preAverage;
    public String preDate;
    public String preDateMiladi;
    public Integer preCounterState;
    public Integer vaziatCode;
    public String counterSerial;
    public String tavizDate;
    public Integer zarfiat;
    public Integer ahadMasraf;//TODO
    //endregion

    //region Offload members
    public Integer counterNumber;
    public Integer counterStateCode;
    public Integer counterStatePosition;
    public String possibleCounterSerial;//// TODO: 1/4/2018 navigator
    public String possibleAddress;//// TODO: 1/4/2018 navigator
    public String possibleEshterak;//// TODO: 1/4/2018 navigator
    public String possibleMobile;//// TODO: 1/4/2018 navigator
    public String possiblePhoneNumber;//// TODO: 1/4/2018 navigator
    public Integer tedadKhali;//// TODO: 1/4/2018 navigator
    public Integer possibleTedadMaskooni;
    public Integer possibleTedadTejari;
    public Integer possibleKarbariCode;
    public Double latitude;
    public Double longitude;
    public Integer gisAccuracy;
    public Integer masrafState;
    public Integer masraf;
    public Float newRate;
    public Integer dateDifference;
    public Integer highLowState;
    public Integer offloadedCount;
    public String registerDate;
    public String registerDateJalali;
    public String description;//TODO TOZIH
    public Integer highLowAlgorithmId;
    public Integer highLowStateId;
    public Boolean isCounterNumberShown;
    public Boolean isBazdid;
    public boolean isArchive;
    public Integer d1;
    public Integer d2;
    public Integer l1;
    public Integer l2;

    public OnOffLoad() {
    }

    public OnOffLoad(OnLoad onLoad, OffLoad offLoad, String idCusotm) {
        setOffLoad(offLoad);
        setOnLoad(onLoad);
        this.idCusotm = idCusotm;
        offLoadStateId = OffloadStateEnum.INSERTED.getValue();
        isArchive = false;
    }

    private void setOnLoad(OnLoad onLoad) {
        zoneId = onLoad.zoneId;
        listNumber = onLoad.listNumber;
        trackNumber = onLoad.trackNumber;
        billId = onLoad.billId;
        radif = onLoad.radif;
        eshterak = onLoad.eshterak;
        qeraatCode = onLoad.qeraatCode;
        name = onLoad.name;
        family = onLoad.family;
        address = onLoad.address;
        karbariCode = onLoad.karbariCode;
        tedadMaskooni = onLoad.tedadMaskooni;
        tedadNonMaskooni = onLoad.tedadNonMaskooni;
        tedadKol = onLoad.tedadKol;
        qotr = onLoad.qotr;
        sifoonQotr = onLoad.sifoonQotr;
        preNumber = onLoad.preNumber;
        preAverage = onLoad.preAverage;
        preDate = onLoad.preDate;
        preDateMiladi = onLoad.preDateMiladi;
        preCounterState = onLoad.preCounterState;
        vaziatCode = onLoad.vaziatCode;
        counterSerial = onLoad.counterSerial;
        tavizDate = onLoad.tavizDate;
        zarfiat = onLoad.zarfiat;
        ahadMasraf = onLoad.ahadMasraf;
    }

    public String getQotrCustom() {
        if (qotr == null || qotr > 13)
            return "";
        String[] qotrList = {"", "1/2", "3/4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        return qotrList[qotr];

    }

    public String getSifoonQotrCustom() {
        if (sifoonQotr == null || sifoonQotr > 3)
            return "";
        String[] qotrList = {"", "100", "125", "200"};
        return qotrList[sifoonQotr];
    }

    public OffLoadParams getOffLoad() {
        OffLoadParams offLoadParams = new OffLoadParams();
        offLoadParams.Id = idCusotm;
        offLoadParams.counterNumber = counterNumber;
        offLoadParams.counterStateCode = counterStateCode;
        offLoadParams.counterStatePosition = counterStatePosition;
        offLoadParams.possibleAddress = possibleAddress;
        offLoadParams.possibleCounterSerial = possibleCounterSerial;
        offLoadParams.possibleEshterak = possibleEshterak;
        offLoadParams.possibleMobile = possibleMobile;
        offLoadParams.possiblePhoneNumber = possiblePhoneNumber;
        offLoadParams.possibleTedadMaskooni = possibleTedadMaskooni;
        offLoadParams.possibleTedadTejari = possibleTedadTejari;
        offLoadParams.possibleKarbariCode = possibleKarbariCode;
        offLoadParams.latitude = latitude;
        offLoadParams.longitude = longitude;
        offLoadParams.gisAccuracy = gisAccuracy;
        offLoadParams.masrafState = masrafState;
        offLoadParams.isCounterNumberShown = isCounterNumberShown;
        offLoadParams.highLowState = highLowState;
        offLoadParams.tedadKhali = tedadKhali;
        offLoadParams.offloadedCount = offloadedCount;
        offLoadParams.isBazdid = isBazdid;
        offLoadParams.description = description;
        offLoadParams.highLowAlgorithmId = highLowAlgorithmId;
        offLoadParams.highLowStateId = highLowStateId;
        offLoadParams.trackNumber = trackNumber;
        offLoadParams.l1 = l1;
        offLoadParams.l2 = l2;
        offLoadParams.d1 = d1;
        offLoadParams.d2 = d2;
        return offLoadParams;
    }

    private void setOffLoad(OffLoad offLoad) {
        counterNumber = offLoad.counterNumber;
        counterStateCode = offLoad.counterStateCode;
        counterStatePosition = offLoad.counterStatePosition;
        possibleAddress = offLoad.possibleAddress;
        possibleCounterSerial = offLoad.possibleCounterSerial;
        possibleEshterak = offLoad.possibleEshterak;
        possibleMobile = offLoad.possibleMobile;
        possiblePhoneNumber = offLoad.possiblePhoneNumber;
        possibleTedadMaskooni = offLoad.possibleTedadMaskooni;
        possibleTedadTejari = offLoad.possibleTedadTejari;
        possibleKarbariCode = offLoad.possibleKarbariCode;
        latitude = offLoad.latitude;
        longitude = offLoad.longitude;
        gisAccuracy = offLoad.gisAccuracy;
        masrafState = offLoad.masrafState;
        masraf = offLoad.masraf;
        newRate = offLoad.newRate;
        dateDifference = offLoad.dateDifference;
        isCounterNumberShown = offLoad.isCounterNumberShown;
        highLowState = offLoad.highLowState;
        tedadKhali = offLoad.tedadKhali;
        offloadedCount = offLoad.offloadedCount;
        registerDate = offLoad.offLoadDateTime;
        registerDateJalali = offLoad.offLoadDateTime;
        description = offLoad.description;
        isBazdid = offLoad.isBazdid;
        highLowAlgorithmId = offLoad.highLowAlgorithmId;
        highLowStateId = offLoad.highLowStateId;
    }
}
