package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.leon.abfa.Activities.LocationActivity;
import com.app.leon.abfa.Adapters.SpinnerGisAdapter;
import com.app.leon.abfa.Models.ViewModels.SpinnerDataModel;
import com.app.leon.abfa.R;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.DrawStatus;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedEvent;
import com.esri.arcgisruntime.mapping.view.DrawStatusChangedListener;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LocationFragment extends Fragment {
    Unbinder unbinder;
    Context context;
    private final double SCALE = 700;
    @BindView(R.id.baseMapSpinner)
    Spinner baseMapSpinner;
    @BindView(R.id.mapNavigationSpinner)
    Spinner navigationSpinner;
    @BindView(R.id.mapViewLayout)
    MapView mapView;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.progressBarMapLoading)
    ProgressBar progressBarMapLoading;
    private LocationDisplay locationDisplay;
    private ArcGISMap map;
    private Basemap baseMapTSWBoundary;
    private LayerList mOperationalLayers;
    private ArcGISTiledLayer tswBoundaryTiledLayer;
    private Layer manholeLayer, sewerPipeLayer, networkPipeLayer, commonBlockLayer, streetLayer, parcelLayer;
    private FeatureLayer counterLayer;
    private MenuItem manholeMenu, sewagePipeMenu, networkPipeMenu, eshterakBlockMenu,
            streetMenu, parcelMenu, counterMenu;
    private ServiceFeatureTable counterFeatureTable;
    private ArcGISFeature mIdentifiedFeature;
    private boolean mFeatureSelected = false;
    private View view;
    private boolean firstTry = true;

    public LocationFragment() {
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8277465837,none,8SH93PJPXMH2NERL1236");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws RuntimeException {
        view = inflater.inflate(R.layout.location_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initializeMap(view);
        setHasOptionsMenu(true);
        initializeChangeBaseMapSpinner(view);
        locationDisplay = mapView.getLocationDisplay();
        if (locationDisplay == null) {
            Log.e("loc display", " is null");
        }
        initializeSearchView();
        initializeNavigationItems(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    private void setAutoPan() {
        try {
            locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            if (!locationDisplay.isStarted()) {
                locationDisplay.startAsync();
            }
        } catch (Exception e) {
            Log.e(getTag(), e.getMessage());
        }
    }

    protected void initializeSearchView() {
        searchView.setQueryHint(getString(R.string.search));
        searchView.setQuery(((LocationActivity) getActivity()).onOffLoad.eshterak, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryAndSelectFeature(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.resume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        initializeMenuItems(menu);
        setMenusChecked();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        handleMenuItemSelected(itemId);
        return super.onOptionsItemSelected(item);
    }


    protected void initializeMap(View rootView) {
        initializeViewElements(rootView);

        tswBoundaryTiledLayer = new ArcGISTiledLayer(getString(R.string.tsw_boundary));
        baseMapTSWBoundary = new Basemap(tswBoundaryTiledLayer);
        map = new ArcGISMap(baseMapTSWBoundary);
        initializeServiceFeatureTable();
        initializeSubLayers();

        mOperationalLayers = map.getOperationalLayers();
        addSubLayers();

        mapView.setMap(map);
        mapDrawingStateChangeListener();

        mapView.setMagnifierEnabled(true);

        mapView.setCanMagnifierPanMap(true);
        mapOnTouchListener();
    }

    private void initializeViewElements(View rootView) {
        //mapView = (MapView) rootView.findViewById(R.id.mapViewLayout);
        //progressBarMapLoading = (ProgressBar) rootView.findViewById(R.id.progressBarMapLoading);
    }

    private void initializeServiceFeatureTable() {
        counterFeatureTable = new ServiceFeatureTable(getString(R.string.counter_feature_service));
        counterFeatureTable.setFeatureRequestMode(ServiceFeatureTable.FeatureRequestMode.ON_INTERACTION_CACHE);
    }

    private void initializeSubLayers() {
        manholeLayer = new ArcGISTiledLayer(getString(R.string.sewage_manhole));
        sewerPipeLayer = new ArcGISTiledLayer(getString(R.string.sewage_pipe));
        networkPipeLayer = new ArcGISTiledLayer(getString(R.string.network_pipe));
        commonBlockLayer = new ArcGISTiledLayer(getString(R.string.eshterak_block));
        streetLayer = new ArcGISTiledLayer(getString(R.string.street));
        parcelLayer = new ArcGISTiledLayer(getString(R.string.parcel));

        counterLayer = new FeatureLayer(counterFeatureTable);
        ((FeatureLayer) counterLayer).setSelectionColor(Color.CYAN);
        ((FeatureLayer) counterLayer).setSelectionWidth(6);
    }

    private void addSubLayers() {
        //mOperationalLayers.add(manholeLayer);
        //mOperationalLayers.add(sewerPipeLayer);
        //mOperationalLayers.add(networkPipeLayer);
        mOperationalLayers.add(commonBlockLayer);
        //mOperationalLayers.add(streetLayer);
        //mOperationalLayers.add(parcelLayer);
        mOperationalLayers.add(counterLayer);//todo added temp
    }

    private void initializeMenuItems(Menu menu) {
        manholeMenu = menu.getItem(0);
        sewagePipeMenu = menu.getItem(1);
        networkPipeMenu = menu.getItem(2);
        eshterakBlockMenu = menu.getItem(3);
        streetMenu = menu.getItem(4);
        parcelMenu = menu.getItem(5);
        counterMenu = menu.getItem(6);
    }

    private void setMenusChecked() {
        manholeMenu.setChecked(false);
        sewagePipeMenu.setChecked(false);
        networkPipeMenu.setChecked(false);
        eshterakBlockMenu.setChecked(true);
        streetMenu.setChecked(false);
        parcelMenu.setChecked(false);
        counterMenu.setChecked(true);
    }

    private void handleMenuItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.manhole_menu:
                selectOrDeselectMenuItem(manholeMenu, manholeLayer);
                break;
            case R.id.sewer_pipe_menu:
                selectOrDeselectMenuItem(sewagePipeMenu, sewerPipeLayer);
                break;
            case R.id.network_pipe_menu:
                selectOrDeselectMenuItem(networkPipeMenu, networkPipeLayer);
                break;
            case R.id.eshterak_block_menu:
                selectOrDeselectMenuItem(eshterakBlockMenu, commonBlockLayer);
                break;
            case R.id.street_menu:
                selectOrDeselectMenuItem(streetMenu, streetLayer);
                break;
            case R.id.parcel_menu:
                selectOrDeselectMenuItem(parcelMenu, parcelLayer);
                break;
            case R.id.counter_menu:
                selectOrDeselectMenuItem(counterMenu, counterLayer);
                break;
            default:
//                throw new RuntimeException("no menu find!");
                getActivity().finish();
        }
    }

    private void selectMenuItem(MenuItem menuItem, Layer subLayer) {
        map.getOperationalLayers().add(subLayer);
        menuItem.setChecked(true);
    }

    private void unSelectMenuItem(MenuItem menuItem, Layer subLayer) {
        map.getOperationalLayers().remove(subLayer);
        menuItem.setChecked(false);
    }

    private void selectOrDeselectMenuItem(MenuItem menuItem, Layer subLayer) {
        if (menuItem.isChecked()) {
            unSelectMenuItem(menuItem, subLayer);
        } else {
            selectMenuItem(menuItem, subLayer);
        }
    }

    private void mapDrawingStateChangeListener() {
        mapView.addDrawStatusChangedListener(new DrawStatusChangedListener() {
            @Override
            public void drawStatusChanged(DrawStatusChangedEvent drawStatusChangedEvent) {
                if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.IN_PROGRESS) {
                    progressBarMapLoading.setVisibility(View.VISIBLE);
                } else if (drawStatusChangedEvent.getDrawStatus() == DrawStatus.COMPLETED) {
                    progressBarMapLoading.setVisibility(View.INVISIBLE);
                    if (firstTry)
                        queryAndSelectFeature(((LocationActivity) getActivity()).onOffLoad.eshterak);
                    firstTry = false;
                }
            }
        });
    }

    private void mapOnTouchListener() {
        try {
            mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getActivity(), mapView) {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {

                    if (!mFeatureSelected) {
                        android.graphics.Point screenCoordinate = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
                        double tolerance = 20;

                        final ListenableFuture<IdentifyLayerResult> identifyFuture = mMapView.identifyLayerAsync(counterLayer, screenCoordinate, tolerance, false, 1);
                        identifyFuture.addDoneListener(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    IdentifyLayerResult layerResult = identifyFuture.get();
                                    List<GeoElement> resultGeoElements = layerResult.getElements();

                                    if (resultGeoElements.size() > 0) {
                                        if (resultGeoElements.get(0) instanceof ArcGISFeature) {
                                            mIdentifiedFeature = (ArcGISFeature) resultGeoElements.get(0);
                                            //Select the identified feature
                                            counterLayer.selectFeature(mIdentifiedFeature);
                                            mFeatureSelected = true;
                                            Toast.makeText(getActivity(), "فیچر انتخاب شد  ،برای اعمال تغییرات اطلاعات مکانی روی نقشه تپ کنید", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getActivity(), "هیچ فیچری انتخاب نشده ، برای انتخاب تپ کنید", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (InterruptedException | ExecutionException e) {
                                    Log.e(getResources().getString(R.string.app_name), "به روز رسانی ناموفق: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        Point movedPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
                        final Point normalizedPoint = (Point) GeometryEngine.normalizeCentralMeridian(movedPoint);
                        mIdentifiedFeature.addDoneLoadingListener(new Runnable() {
                            @Override
                            public void run() {

                                boolean canIEdit = mIdentifiedFeature.canEditAttachments();
                                boolean canUpdateGeometr = mIdentifiedFeature.canUpdateGeometry();
                                if (!canUpdateGeometr) {
                                    Toast.makeText(getActivity(), "این فیچر قابلیت به روز رسانی ندارد", Toast.LENGTH_SHORT);
                                    return;
                                } else {
                                    mIdentifiedFeature.setGeometry(normalizedPoint);
                                    final ListenableFuture<Void> updateFuture = counterLayer.getFeatureTable().updateFeatureAsync(mIdentifiedFeature);
                                    updateFuture.addDoneListener(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {

                                                updateFuture.get();

                                                if (updateFuture.isDone()) {
                                                    applyEditsToServer();
                                                    counterLayer.clearSelection();
                                                    mFeatureSelected = false;
                                                } else {
                                                    Log.e(getResources().getString(R.string.app_name), "به روز رسانی ناموفق");
                                                }
                                            } catch (InterruptedException | ExecutionException e) {
                                                Log.e(getResources().getString(R.string.app_name), "علت به روز رسانی ناموفق: " + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        mIdentifiedFeature.loadAsync();
                    }
                    return super.onSingleTapConfirmed(e);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getActivity(), "بنظر میرسد این فیچر از به روز رسانی پشتیبانی نمیکند", Toast.LENGTH_SHORT);
        }

    }

    private void applyEditsToServer() {
        final ListenableFuture<List<FeatureEditResult>> applyEditsFuture =
                ((ServiceFeatureTable) counterLayer.getFeatureTable()).applyEditsAsync();
        applyEditsFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {

                    List<FeatureEditResult> featureEditResultsList = applyEditsFuture.get();
                    if (!featureEditResultsList.get(0).hasCompletedWithErrors()) {
                        Toast.makeText(getActivity(), "تغییرات مورد نظر شما اعمال شد. ObjectID: " + featureEditResultsList.get(0).getObjectId(), Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException | ExecutionException e) {
                    Log.e(getResources().getString(R.string.app_name), "به روز رسانی ناموفق: " + e.getMessage());
                }
            }
        });
    }

    //
    private void initializeChangeBaseMapSpinner(View rootView) {
        baseMapSpinner = (Spinner) rootView.findViewById(R.id.baseMapSpinner);
        addBaseMapGisSpinner();
    }

    private void addBaseMapGisSpinner() {
        ArrayList<SpinnerDataModel> list = new ArrayList<>();
        list.add(new SpinnerDataModel(getString(R.string.local_gis_basemap), R.drawable.locationdisplaydisabled));//// TODO: change image
        list.add(new SpinnerDataModel(getString(R.string.osm), R.drawable.locationdisplayon));//// TODO: change image

        SpinnerGisAdapter adapter = new SpinnerGisAdapter(getActivity(), R.layout.gis_pan_mode_spinner_layout, R.id.txt, list);
        baseMapSpinner.setAdapter(adapter);
        baseMapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        map.setBasemap(baseMapTSWBoundary);
                        break;
                    case 1:
                        map.setBasemap(Basemap.createOpenStreetMap());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    //
    private void initializeNavigationItems(View rootView) {
        addNavigationSpinner();
        setNavigationSpinnerClickListener();
        locationChangeStateListener();
    }

    private void addNavigationSpinner() {
        ArrayList<SpinnerDataModel> list = new ArrayList<>();
        list.add(new SpinnerDataModel("خاموش", R.drawable.locationdisplaydisabled));
        list.add(new SpinnerDataModel("روشن", R.drawable.locationdisplayon));
        list.add(new SpinnerDataModel("Re-Center", R.drawable.locationdisplayrecenter));
        list.add(new SpinnerDataModel("ناوبری", R.drawable.locationdisplaynavigation));
        list.add(new SpinnerDataModel("قطب نما", R.drawable.locationdisplayheading));

        SpinnerGisAdapter adapter = new SpinnerGisAdapter(getActivity(), R.layout.gis_pan_mode_spinner_layout, R.id.txt, list);
        navigationSpinner.setAdapter(adapter);
    }

    private void setNavigationSpinnerClickListener() {
        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        if (locationDisplay.isStarted())
                            locationDisplay.stop();
                        break;
                    case 1:
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                    case 2:
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                    case 3:
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                    case 4:
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                        if (!locationDisplay.isStarted())
                            locationDisplay.startAsync();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void locationChangeStateListener() {
        locationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {


                if (dataSourceStatusChangedEvent.isStarted()) {
                    return;
                }

                if (dataSourceStatusChangedEvent.getError() == null) {
                    return;
                }

                String message = String.format("خطا در DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                        .getSource().getLocationDataSource().getError().getMessage());
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                navigationSpinner.setSelection(0, true);
            }
        });

    }

    private void queryAndSelectFeature(CharSequence eshterak) {
        counterLayer.clearSelection();

        // create objects required to do a selection with a query
        final QueryParameters query = new QueryParameters();
        //make search case insensitive
        query.setWhereClause("upper(Eshterak_Code) LIKE '%" + eshterak + "%'");//1203102450 for test

        final ListenableFuture<FeatureQueryResult> future = counterFeatureTable.queryFeaturesAsync(query);
        // add done loading listener to fire when the selection returns
        future.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // call get on the future to get the result
                    FeatureQueryResult result = future.get();

                    // check there are some results
                    if (result.iterator().hasNext()) {

                        // get the extend of the first feature in the result to zoom to with the default scale
                        Feature feature = result.iterator().next();
                        Envelope envelope = feature.getGeometry().getExtent();
                        mapView.setViewpointGeometryAsync(envelope, 200);
                        Geometry geometry = feature.getGeometry();
                        mapView.setViewpointGeometryAsync(geometry);
                        mapView.setViewpointScaleAsync(SCALE);
                        //Select the feature
                        counterLayer.selectFeature(feature);
                        mIdentifiedFeature = (ArcGISFeature) feature;
                        mFeatureSelected = true;
                    } else {
                        Log.e("gis", "eshterak not found");
                        setAutoPan();
                    }
                } catch (Exception e) {
                    Log.e(getResources().getString(R.string.app_name), "Feature search failed for: " + query + ". Error=" + e.getMessage());
                    setAutoPan();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }
}

